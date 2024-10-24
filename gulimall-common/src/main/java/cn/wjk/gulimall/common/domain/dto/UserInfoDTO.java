package cn.wjk.gulimall.common.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.common.domain.to
 * @ClassName: UserInfoDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午9:43
 * @Description: 存储用户登录信息
 */
@Data
public class UserInfoDTO {
    private Long userId;
    private String userKey;
}
