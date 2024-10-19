package cn.wjk.gulimall.common.domain.to;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.common.domain.to
 * @ClassName: UserRegisterTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午4:43
 * @Description: 用户注册的TO
 */
@Data
public class UserRegisterTO {
    private String username;
    private String password;
    private String phone;
}
