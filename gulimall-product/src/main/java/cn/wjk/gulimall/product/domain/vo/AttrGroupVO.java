package cn.wjk.gulimall.product.domain.vo;

import lombok.Data;

/**
 * 属性分组
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@Data
public class AttrGroupVO {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 当前属性所在category相对于一级category的的全路径
     */
    private Long[] catelogPath;
}
