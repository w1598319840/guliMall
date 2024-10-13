package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: WareFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午2:51
 * @Description: 仓储服务的远程调用feign
 */
@FeignClient("gulimall-ware")
public interface WareFeign {
    /**
     * 获取sku的库存数量
     */
    @GetMapping("/ware/waresku/stock")
    R getSkuStock(@RequestParam("skuIds") List<Long> skuIds);
}
