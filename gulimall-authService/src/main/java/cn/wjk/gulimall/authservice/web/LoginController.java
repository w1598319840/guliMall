package cn.wjk.gulimall.authservice.web;

import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.constant.AuthConstants;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

/**
 * @Package: cn.wjk.gulimall.authservice.web
 * @ClassName: LoginController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/22 下午2:34
 * @Description:
 */
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(AuthConstants.LOGIN_USER) != null) {
            //登录过了
            return "redirect:http://gulimall.com";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated UserLoginDTO userLoginDTO, Model model, HttpSession session) {
        MemberVO memberVO = authService.login(userLoginDTO);
        //一样的，有错误直接抛异常
        model.addAttribute("errors", Collections.emptyMap());
        session.setAttribute(AuthConstants.LOGIN_USER, memberVO);
        return "redirect:http://gulimall.com";
    }

    @GetMapping("/oauth2.0/github/success")
    public String githubOAuth(@RequestParam("code") String code, HttpSession session) {
        MemberVO memberVO = authService.githubOAuth(code);
        session.setAttribute(AuthConstants.LOGIN_USER, memberVO);
        return "redirect:http://gulimall.com";
    }
}
