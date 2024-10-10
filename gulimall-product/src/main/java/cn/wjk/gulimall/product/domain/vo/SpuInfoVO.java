package cn.wjk.gulimall.product.domain.vo;

import cn.wjk.gulimall.product.domain.entity.SpuInfoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Package: cn.wjk.gulimall.product.domain.vo
 * @ClassName: SpuInfoVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/10 下午6:43
 * @Description: Spu信息的VO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SpuInfoVO extends SpuInfoEntity {
    private String brandName;
    private String catalogName;
}
