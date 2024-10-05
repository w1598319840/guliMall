package cn.wjk.gulimall.common.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.entity.vo
 * @ClassName: CategoryVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/4 下午8:28
 * @Description: 分类的VO
 */
@Data
public class CategoryVO {
    /**
     * 分类id
     */
    private Long catId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 父分类id
     */
    private Long parentCid;
    /**
     * 层级
     */
    private Integer catLevel;
    /**
     * 是否显示[0-不显示，1显示]
     */
    private Integer showStatus;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标地址
     */
    private String icon;
    /**
     * 计量单位
     */
    private String productUnit;
    /**
     * 商品数量
     */
    private Integer productCount;
    /**
     * 子分类
     */
    private List<CategoryVO> children;

}
