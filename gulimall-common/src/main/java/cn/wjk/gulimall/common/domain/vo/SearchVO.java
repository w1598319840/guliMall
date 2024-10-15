package cn.wjk.gulimall.common.domain.vo;

import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.domain.vo
 * @ClassName: SearchVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 下午7:18
 * @Description: 商品查询返回给页面的VO
 */
@Data
public class SearchVO {
    /**
     * 所有商品信息
     */
    private List<SkuEsModel> products;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;
    /**
     * 当前查询到的结果所有涉及的品牌
     */
    private List<BrandVO> brands;
    /**
     * 当前查询到的结果所有涉及的属性
     */
    private List<AttrVO> attrs;
    /**
     * 当前查询到的结果所有设计的分类
     */
    private List<CatalogVO> catalogs;

    @Data
    private static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    private static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    private static class CatalogVO {
        private Long catalogId;
        private String catalogName;
    }
}
