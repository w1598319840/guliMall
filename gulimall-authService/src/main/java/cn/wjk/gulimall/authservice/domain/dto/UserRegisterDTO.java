package cn.wjk.gulimall.authservice.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Package: cn.wjk.gulimall.authservice.domain.dto
 * @ClassName: UserRegisterDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午3:34
 * @Description: 用户注册时使用的DTO
 */
@Data
public class UserRegisterDTO {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6, max = 18, message = "用户名长度必须在${min}~${max}之间")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 18, message = "密码长度必须在${min}~${max}之间")
    private String password;
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "不合法的手机号")
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    private String code;
}
