package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: ProductFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午2:51
 * @Description: 产品服务的远程调配feign
 */
@FeignClient("gulimall-product")
public interface ProductFeign {
    @GetMapping("/product/skuinfo/info/skuName")
    R getSkuNamesBySkuIds(@RequestParam("skuIds") List<Long> skuIds);

    @GetMapping("/product/attr/name")
    R getAttrNameByAttrIds(@RequestParam("attrIds") List<Long> attrIds);
}
