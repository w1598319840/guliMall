package cn.wjk.gulimall.common.feign;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import cn.wjk.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.feign
 * @ClassName: SearchFeign
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午7:44
 * @Description: ES相关服务的Feign
 */
@FeignClient("gulimall-search")
public interface SearchFeign {
    /**
     * 上架商品
     */
    @PostMapping("/search/save")
    R up(@RequestBody List<SkuEsModel> skuEsModels);
}
