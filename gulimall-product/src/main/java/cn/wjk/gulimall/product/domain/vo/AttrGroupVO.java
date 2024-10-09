package cn.wjk.gulimall.product.domain.vo;

import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 属性分组
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AttrGroupVO extends AttrGroupEntity {
    /**
     * 当前属性所在category相对于一级category的的全路径
     */
    private Long[] catelogPath;

    /**
     * 该分组下的所有属性
     */
    private List<AttrEntity> attrs;
}
