package cn.wjk.gulimall.authservice.web;

import cn.wjk.gulimall.authservice.domain.dto.UserRegisterDTO;
import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import cn.wjk.gulimall.common.utils.R;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

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
@Slf4j
public class AuthController {
    private final AuthService authService;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        authService.sendCode(phone);
        return R.ok();
    }

    @PostMapping("/register")
    public String register(@Validated UserRegisterDTO userRegisterDTO, Model model) {
        //前后端不分离真是傻逼，不然直接抛出异常让GlobalExceptionHandler处理就可以了
        //因此我打算还是让GlobalExceptionHandler处理异常，虽然这里不再是返回Json了，管他呢
        model.addAttribute("errors", Collections.emptyMap());
        authService.register(userRegisterDTO);

        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated UserLoginDTO userLoginDTO, Model model) {
        authService.login(userLoginDTO);
        //一样的，有错误直接抛异常
        model.addAttribute("errors", Collections.emptyMap());
        return "redirect:http://gulimall.com";
    }

    @GetMapping("/oauth2.0/github/success")
    public String githubOAuth(@RequestParam("code") String code, HttpSession session) {
        MemberEntity memberEntity = authService.githubOAuth(code);
        session.setAttribute("loginUser", memberEntity);
        return "redirect:http://gulimall.com";
    }
}
