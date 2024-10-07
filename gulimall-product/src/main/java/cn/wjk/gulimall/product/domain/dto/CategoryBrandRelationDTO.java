package cn.wjk.gulimall.product.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.product.domain.dto
 * @ClassName: CategoryBrandRelationDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/7 上午11:27
 * @Description: 品牌分类关系的DTO
 */
@Data
public class CategoryBrandRelationDTO {
    private Long brandId;
    private Long catelogId;
}
