package cn.wjk.gulimall.product.domain.vo;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.product.domain.vo
 * @ClassName: CategoryVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/5 下午3:18
 * @Description:
 */
@Data
public class CategoryVO {
    private Long catId;
    private String name;
    private String icon;
    private String productUnit;
}
