package cn.wjk.gulimall.common.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: OrderFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午2:51
 * @Description: 订单服务的远程调用feign
 */
@FeignClient("gulimall-order")
public interface OrderFeign {
}
