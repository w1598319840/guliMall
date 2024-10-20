package cn.wjk.gulimall.common.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.authservice.domain.dto
 * @ClassName: UserLoginTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/20 下午2:18
 * @Description: 用户登录时使用的DTO
 */
@Data
public class UserLoginDTO {
    @NotNull
    private String loginacct;
    @NotNull
    private String password;
}
