package cn.wjk.gulimall.search.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.search.domain.dto
 * @ClassName: SearchDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 下午6:55
 * @Description: 搜索条件的VO
 */
@Data
public class SearchDTO {
    /**
     * 关键字
     * keyword=xxx
     */
    private String keyword;
    /**
     * 三级分类id
     * catalog3Id=213
     */
    private Long catalog3Id;
    /**
     * 排序条件
     * sort=saleCount_asc/skuPrice_asc/hotScore_desc
     */
    private String sort;
    /**
     * 是否有货
     * hasStock=0/1
     */
    private Integer hasStock;
    /**
     * 价格区间
     * skuPrice=1_500/_500/500_
     */
    private String skuPrice;
    /**
     * 品牌
     * brandId=24,214,21
     */
    private List<Long> brandId;
    /**
     * 属性(多选)
     * attrs=1_其他:安卓&attrs=2_5寸:6寸 (一号属性选了其他和安装两个选项)
     */
    private List<String> attrs;
    /**
     * 页码
     */
    private Integer pageNum;
}
