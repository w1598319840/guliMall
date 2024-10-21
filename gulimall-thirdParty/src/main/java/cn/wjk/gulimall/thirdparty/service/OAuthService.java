package cn.wjk.gulimall.thirdparty.service;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.GithubUserDTO;

/**
 * @Package: cn.wjk.gulimall.thirdparty.service
 * @ClassName: OAuthService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午2:27
 * @Description:
 */
public interface OAuthService {
    /**
     * 根据code获取access_key
     */
    GithubOAuthDTO getGithubOAuthAccessKey(String code) throws Exception;

    /**
     * 根据access_key获取用户信息
     */
    GithubUserDTO getGithubUserInfo(String accessToken) throws Exception;
}
