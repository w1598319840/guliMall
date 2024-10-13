package cn.wjk.gulimall.common.domain.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.domain.to.es
 * @ClassName: SkuEsModel
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午3:52
 * @Description: Sku在Es中保存的信息
 */
@Data
public class SkuEsModel {
    private Long skuId;//sku_info
    private Long spuId;//sku_info
    private String skuTitle;//sku_info
    private BigDecimal skuPrice;//sku_info
    private String skuImg;//sku_info
    private Long saleCount;//sku_info
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;//sku_info
    private String brandName;//brand
    private String brandImg;//brand
    private Long catalogId;//sku_info
    private String catalogName;//category
    private List<AttrEsModel> attrs;//sku_sale_attr_value
}
