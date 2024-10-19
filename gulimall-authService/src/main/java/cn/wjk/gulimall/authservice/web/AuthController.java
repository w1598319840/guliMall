package cn.wjk.gulimall.authservice.web;

import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Package: cn.wjk.gulimall.authservice.web
 * @ClassName: AuthController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午4:44
 * @Description: 用户权限认证的controller
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        authService.sendCode(phone);
        return R.ok();
    }
}
