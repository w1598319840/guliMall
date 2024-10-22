package cn.wjk.gulimall.authservice.web;

import cn.wjk.gulimall.authservice.domain.dto.UserRegisterDTO;
import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.utils.R;
import lombok.RequiredArgsConstructor;
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
 * @ClassName: RegisterController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/22 下午2:33
 * @Description:
 */
@Controller
@RequiredArgsConstructor
public class RegisterController {
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
}
