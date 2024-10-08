package cn.wjk.gulimall.product.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.product.domain.dto
 * @ClassName: AttrAttrgroupRelationDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/8 下午7:07
 * @Description: 属性以及其分组关联表
 */
@Data
public class AttrAttrgroupRelationDTO {
    private Long attrId;
    private Long attrGroupId;
}
