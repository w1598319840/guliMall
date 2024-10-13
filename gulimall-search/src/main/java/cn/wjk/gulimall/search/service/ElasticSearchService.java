package cn.wjk.gulimall.search.service;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.search.service
 * @ClassName: ElasticSearchService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午7:46
 * @Description:
 */
public interface ElasticSearchService {
    /**
     * 上架商品
     */
    boolean up(List<SkuEsModel> skuEsModels) throws IOException;
}
