package cn.wjk.gulimall.product.domain.vo;

import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品属性
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-08 10:19:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AttrVO extends AttrEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属分类的名称
     */
    private String catelogName;
    /**
     * 所属分组的id
     */
    private Long attrGroupId;
    /**
     * 所属分组的名称
     */
    private String groupName;
    /**
     * 当前规格参数的分类的全路径
     */
    private Long[] catelogPath;
}
