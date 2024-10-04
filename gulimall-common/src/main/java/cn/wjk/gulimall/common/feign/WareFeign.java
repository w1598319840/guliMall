package cn.wjk.gulimall.common.feign;

import org.springframework.cloud.openfeign.FeignClient;

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
}
