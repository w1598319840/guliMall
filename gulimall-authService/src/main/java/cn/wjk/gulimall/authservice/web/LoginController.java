package cn.wjk.gulimall.authservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Package: cn.wjk.gulimall.authservice.web
 * @ClassName: LoginController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午4:44
 * @Description: 登录
 */
@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/")
    public String login() {
        return "login";
    }
}
