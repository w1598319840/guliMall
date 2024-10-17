package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.product.domain.vo.SkuItemVO;

/**
 * @Package: cn.wjk.gulimall.product.service
 * @ClassName: ItemService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/17 下午8:36
 * @Description: 商品详情的service
 */
public interface ItemService {
    /**
     * 查询某一sku的商品详细信息
     */
    SkuItemVO getItemDetail(Long skuId);
}
