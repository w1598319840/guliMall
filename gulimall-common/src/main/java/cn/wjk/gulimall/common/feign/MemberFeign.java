package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: MemberFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午2:51
 * @Description: 会员服务的远程调用feign
 */
@FeignClient("gulimall-member")
public interface MemberFeign {
    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterTO userRegisterTO);
}
