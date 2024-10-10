package cn.wjk.gulimall.common.domain.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Package: cn.wjk.gulimall.common.domain.to
 * @ClassName: SpuBoundsTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/10 下午1:47
 * @Description: Spu积分的TO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpuBoundsTO {
    private Long SpuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
