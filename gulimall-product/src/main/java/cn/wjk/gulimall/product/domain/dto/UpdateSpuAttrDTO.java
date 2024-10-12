package cn.wjk.gulimall.product.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.product.domain.dto
 * @ClassName: UpdateSpuAttrDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午1:16
 * @Description: 修改商品规格时的DTO
 */
@Data
public class UpdateSpuAttrDTO {
    private Long attrId;
    private String attrName;
    private String attrValue;
    private Integer quickShow;
}
