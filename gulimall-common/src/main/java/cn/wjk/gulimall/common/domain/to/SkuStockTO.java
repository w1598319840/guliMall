package cn.wjk.gulimall.common.domain.to;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.ware.domain.vo
 * @ClassName: SkuStockTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午6:06
 * @Description: sku以及其库存数量的to
 */
@Data
public class SkuStockTO {
    private Long skuId;
    private Integer stock;
}
