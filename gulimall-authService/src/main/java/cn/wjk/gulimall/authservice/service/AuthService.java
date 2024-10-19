package cn.wjk.gulimall.authservice.service;

import cn.wjk.gulimall.authservice.domain.dto.UserRegisterDTO;

/**
 * @Package: cn.wjk.gulimall.authservice.service
 * @ClassName: AuthService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:15
 * @Description: 权限认证的Service
 */
public interface AuthService {

    /**
     * 发送验证码
     */
    void sendCode(String phone);

    /**
     * 用户注册
     */
    void register(UserRegisterDTO userRegisterDTO);
}
