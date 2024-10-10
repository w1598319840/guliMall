package cn.wjk.gulimall.common.domain.to;

import cn.wjk.gulimall.common.domain.dto.MemberPriceDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.common.domain.to
 * @ClassName: SkuReductionTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/10 下午2:03
 * @Description: Spu的优惠、满减信息的TO
 */
@Data
public class SkuReductionTO {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;//是否叠加其他优惠
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceDTO> memberPrice;
}
