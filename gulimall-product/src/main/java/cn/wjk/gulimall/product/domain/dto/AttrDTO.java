package cn.wjk.gulimall.product.domain.dto;

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
 * @date 2024-10-03 20:19:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AttrDTO extends AttrEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 该规格参数所属的属性分组
     */
    private Long attrGroupId;
}
