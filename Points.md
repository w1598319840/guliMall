# 知识点

## 社交登录(OAuth2.0)流程

以`Github`为例

```mermaid
sequenceDiagram
    participant A as client(CSDN等第三方应用)
    participant B as resource owner(用户本人)
    participant C as Authorization Server(Github认证服务器)
    participant D as Resource Server(Github资源服务器)
    A ->> B: 1. 向用户申请请求认证
    B ->> B: 2. 用户授权(用户输入自己的社交账号的账号密码)
    B ->> C: 3. 发送请求到第三方认证服务器进行认证
    C ->> A: 4. 认证通过，返回code码
    A ->> C: 5. 根据code码获取AccessToken
    C ->> A: 6. 认证code，返回AccessToken
    A ->> D: 7. 使用AccessToken，获取用户开放的信息(头像、用户名、...)
    D ->> A: 8. 认证AccessToken，返回用户开放的信息
```

### 注意

1. 一个code码只能获取一次AccessToken
2. 一个用户的AccessToken在一段时间内不会发生变化，即使使用不同的code码重复获取

## Session在分布式下的问题

1. **跨域问题**: session有自己的作用域，只能在一个域名下有效，域名改变了session就会失效
2. **共享问题**: session保存在实例中，而分布式情况下有一个服务有多台实例

### 解决Session共享问题

1. **hash一致性**: 根据请求进行哈希映射，保证同一个请求永远都只会被负载均衡到同一台服务器上
2. **统一存储**: 将session存储在第三方中间件中(Redis、Database)，可以使用`Spring Session`

### 解决Session跨域问题

1. **子域共享**: 扩大session的作用域，那么当我们访问的路径在session作用域及其子域下时，session都有效

## 使用`Spring Session`

1. 引入`spring-session-data-redis`依赖
2. 在启动类上添加`@EnableRedisHttpSession`注解
3. 此后，所有向`HttpSession`中添加session的操作都会被filter转为向redis中添加session

## 单点登录(sso)

*一处登录，处处登录*  
在新浪官网(sina.com)登录过了，我们在微博(weibo.com)就不需要再次登录一遍，虽然是不同域名，但还是自动登录了

### 问题出现原因

之前我们使用`Spring Session`解决了Session的跨域问题，但那种跨域问题只能解决父域与子域之间(
gulimall.com和auth.gulimall.com)跨域问题，不能解决不同域名之间(taobao.com和tmall.com)
的跨域问题，因为我们不可能让某个Session的domain变成`com`

### 核心

1. 中央认证服务器
2. 其他**域名不同**的系统，这些系统在想要登录时会去中央认证服务器登录，登录成功后再跳转回自己系统
3. 所有系统只要有一个登录，其他都不用登录
4. 全系统统一一个cookie

### 流程图

下面对应基于`session`的`SSO`

```mermaid
sequenceDiagram
    participant A as browser
    participant B as client1.com:8081
    participant C as client2.com:8082
    participant D as sso-server:8080
    A ->> B: 1. 访问受保护的页面
    B ->> B: 2. 判断当前用户是否登录<br>① 如果请求参数中的token值不为null，则表示已登录，<br>并会将用户登录状态信息存入session中<br>② session中有用户登录状态信息，则表示已登录
    B ->> A: 3. 若没登录，命令浏览器`重定向`到认证服务器<br>同时要在地址栏的参数位置带上原系统的url<br>方便登录成功后跳转回来
    A ->> D: 4. 浏览器根据response，访问认证服务器
    D ->> D: 5. 根据cookie(sso_token)判断用户是否登录过了
    D ->> A: 6. 如果用户未登录过，返回中央认证服务器的登录页
    A ->> A: 7. 用户输入账号密码进行登录
    A ->> D: 8. 提交登录请求，参数包括username、password、redirect_url
    D ->> D: 9. 处理登录请求。<br>若登录成功将用户登录状态信息存入redis中。<br>并生成一个cookie(称为`sso_token`)，<br>表示用户在当前认证服务器中登录过了
    D ->> A: 10. 命令浏览器`重定向`到原系统，并在地址栏的参数位置带上用户登录状态信息在redis中的key(称为token)<br>并在`Response Header`中添加`set-cookie`字段，命令浏览器以cookie的形式保存认证服务器生成的`sso_token`
    A ->> A: 11. 浏览器按要求保存cookie。<br>domain为认证服务器的url<br>这样浏览器以后访问认证服务器，<br>都会带上这个cookie了
    A ->> B: 12. 浏览器根据response，跳转到原系统(回步骤1)
    A ->> C: 13. 访问受保护的页面
    C ->> C: 14. 判断当前用户是否登录<br>① 如果请求参数中的token值不为null，则表示已登录，<br>并会将用户登录状态信息存入session中<br>② session中有用户登录状态信息，则表示已登录
    C ->> A: 15. 若没登录，命令浏览器`重定向`到认证服务器<br>同时要在地址栏的参数位置带上原系统的url<br>方便登录成功后跳转回来
    A ->> D: 16. 浏览器根据response，访问认证服务器
    D ->> D: 17. 根据cookie(sso_token)是否存在，<br>判断用户是否登录过了
    D ->> A: 18. 用户登录过了，直接重定向到原系统，并在地址栏的参数位置带上`token=sso_token的value`
    A ->> C: 19. 浏览器根据response，跳转到原系统(回步骤13)
```
#### 注意

其他系统在获取到token后可能还需要向中央认证服务器发送请求，根据token获取对应的用户信息(这和OAuth2.0类似)  
因为用户信息保存在中央认证服务器上，而不是其它系统上