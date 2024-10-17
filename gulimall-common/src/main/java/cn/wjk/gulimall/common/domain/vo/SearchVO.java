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
     * 导航页，用于页面的遍历
     */
    private List<Integer> pageNavs;
    /**
     * 面包屑导航
     */
    private List<NavVO> navs;
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
    /**
     * 所有attr的id
     */
    private List<Long> attrIds;

    @Data
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVO {
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class NavVO {
        private String navName;
        private String navValue;
        private String link;
    }
}
