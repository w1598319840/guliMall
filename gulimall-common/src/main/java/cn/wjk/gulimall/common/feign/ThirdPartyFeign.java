package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: ThirdPartyFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:11
 * @Description: 第三方服务的feign
 */
@FeignClient("gulimall-thirdParty")
public interface ThirdPartyFeign {
    @GetMapping("/sms/send")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

    @GetMapping("/oauth/github/get-access-key/{code}")
    R getGithubOAuthAccessKey(@PathVariable("code") String code);

    @GetMapping("/oauth/github/get-user-info/{accessToken}")
    R getGithubUserInfo(@PathVariable("accessToken") String accessToken);
}
