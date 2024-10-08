package cn.wjk.gulimall.product.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品属性
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-08 10:19:51
 */
@Data
public class AttrVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 值类型[0-为单个值，1-可以选择多个值]
     */
    private Integer valueType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 所属分类名称
     */
    private String catelogName;
    /**
     * 所属分组名称
     */
    private String groupName;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

}
