package cn.wjk.gulimall.thirdparty.service.impl;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.GithubUserDTO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.LoginException;
import cn.wjk.gulimall.common.utils.HttpUtils;
import cn.wjk.gulimall.thirdparty.config.GithubOAuthConfig;
import cn.wjk.gulimall.thirdparty.config.OAuthConfig;
import cn.wjk.gulimall.thirdparty.service.OAuthService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

/**
 * @Package: cn.wjk.gulimall.thirdparty.service.impl
 * @ClassName: OAuthServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午2:28
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
    private final OAuthConfig oAuthConfig;

    @Override
    public GithubOAuthDTO getGithubOAuthAccessKey(String code) throws Exception {
        //1. 使用code，获得access_key
        GithubOAuthConfig githubOAuthConfig = oAuthConfig.getGithubOAuthConfig();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        HashMap<String, String> queries = new HashMap<>();
        queries.put("code", code);
        queries.put("client_id", githubOAuthConfig.getClientId());
        queries.put("client_secret", githubOAuthConfig.getClientSecret());
        queries.put("redirect_uri", githubOAuthConfig.getRedirectUri());
        HttpResponse response = HttpUtils.doPost("https://github.com", "/login/oauth/access_token",
                "post", headers, queries, Collections.emptyMap());
        if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
            throw new LoginException(BizHttpStatusEnum.OAUTH_LOGIN_EXCEPTION);
        }
        InputStream inputStream = response.getEntity().getContent();
        return JSON.parseObject(inputStream, GithubOAuthDTO.class);
    }

    @Override
    public GithubUserDTO getGithubUserInfo(String accessToken) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);
        HttpResponse response = HttpUtils.doGet("https://api.github.com", "/user",
                "get", headers, null);
        String json;
        try (InputStream inputStream = response.getEntity().getContent()) {
            json = new String(inputStream.readAllBytes());
        }
        return JSON.parseObject(json, GithubUserDTO.class);
    }
}
