package cn.wjk.gulimall.thirdparty.controller;

import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.thirdparty.service.OssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: cn.wjk.gulimall.thirdparty.controller
 * @ClassName: OssController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/5 下午7:38
 * @Description: Oss对象存储Controller
 */
@RestController()
@RequiredArgsConstructor
public class OssController {
    private final OssService ossService;

    @RequestMapping("/oss/policy")
    public R policy() {
        return R.ok().put("data", ossService.putOss());
    }
}
