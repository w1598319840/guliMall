package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import cn.wjk.gulimall.common.domain.to.SpuBoundsTO;
import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: CouponFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午2:46
 * @Description: 优惠券服务的远程调用feign
 */
@FeignClient("gulimall-coupon")
public interface CouponFeign {
    /**
     * 保存Spu的Bounds数据
     */
    @RequestMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTO spuBoundsTO);

    @PostMapping("/coupon/skufullreduction/save/reduction")
    R saveSkuReduction(@RequestBody List<SkuReductionTO> skuReductionTO);
}
