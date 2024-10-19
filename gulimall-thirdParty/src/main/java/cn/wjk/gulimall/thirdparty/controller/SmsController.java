package cn.wjk.gulimall.thirdparty.controller;

import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.thirdparty.utils.SmsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Package: cn.wjk.gulimall.thirdparty.controller
 * @ClassName: SmsController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:08
 * @Description: 短信发送controller
 */
@Controller
@RequestMapping("/sms")
@RequiredArgsConstructor
@Slf4j
public class SmsController {
    @SuppressWarnings("all")
    private final SmsUtils smsUtils;

    @GetMapping("/send")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
//        smsUtils.sendCode(phone, code);
        log.info("向手机号{}发送验证码{}成功", phone, code);
        return R.ok();
    }
}
