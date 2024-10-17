package cn.wjk.gulimall.product.domain.vo;

import cn.wjk.gulimall.product.domain.entity.SkuImagesEntity;
import cn.wjk.gulimall.product.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.product.domain.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.product.domain.vo
 * @ClassName: SkuItemVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/17 下午8:28
 * @Description: 商品详细信息的vo
 */
@Data
public class SkuItemVO {
    private SkuInfoEntity info;
    private List<SkuImagesEntity> images;
    private SpuInfoDescEntity desp;
    private List<ItemSaleAttrVO> saleAttr;
    private List<SpuItemAttrGroupVO> groupAttrs;


    @Data
    public static class ItemSaleAttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }

    @Data
    public static class SpuItemAttrGroupVO {
        private String groupName;
        private List<SpuBaseAttrVO> attrs;
    }

    @Data
    public static class SpuBaseAttrVO {
        private String attrName;
        private String attrValue;
    }
}
