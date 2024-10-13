package cn.wjk.gulimall.search.controller;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.search.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.search.controller
 * @ClassName: ElasticSearchController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午7:42
 * @Description:
 */
@RequestMapping("/search")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchController {
    private final ElasticSearchService elasticSearchService;

    /**
     * 上架商品
     */
    @PostMapping("/save")
    public R up(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean hasError;
        BizHttpStatusEnum productUpException = BizHttpStatusEnum.PRODUCT_UP_EXCEPTION;
        try {
            hasError = elasticSearchService.up(skuEsModels);
        } catch (IOException e) {
            log.error("商品上架时发生异常，{}", e.getMessage());
            return R.error(productUpException.getCode(), productUpException.getDesc());
        }
        if (hasError) {
            return R.error(productUpException.getCode(), productUpException.getDesc());
        }
        return R.ok();
    }
}
