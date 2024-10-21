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

