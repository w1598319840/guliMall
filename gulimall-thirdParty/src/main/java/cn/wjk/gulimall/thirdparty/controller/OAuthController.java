package cn.wjk.gulimall.thirdparty.controller;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.GithubUserDTO;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.thirdparty.service.OAuthService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: cn.wjk.gulimall.thirdparty.controller
 * @ClassName: OAuthController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午2:26
 * @Description: 第三方OAuth认证的Controller
 */
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/github/get-access-key/{code}")
    public R getGithubOAuthAccessKey(@PathVariable("code") String code) throws Exception {
        GithubOAuthDTO githubOAuthDTO = oAuthService.getGithubOAuthAccessKey(code);
        return R.ok().put("data", JSON.toJSONString(githubOAuthDTO));
    }

    @GetMapping("github/get-user-info/{accessToken}")
    public R getGithubUserInfo(@PathVariable("accessToken") String accessToken) throws Exception {
        GithubUserDTO githubUserInfo = oAuthService.getGithubUserInfo(accessToken);
        return R.ok().put("data", JSON.toJSONString(githubUserInfo));
    }
}
