package cn.wjk.gulimall.common.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.authservice.domain.dto
 * @ClassName: GithubOAuthDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午1:39
 * @Description: 接收github的oauth返回的access_key等参数
 */
@Data
public class GithubOAuthDTO {
    private String access_token;
    private String scope;
    private String token_type;
}
