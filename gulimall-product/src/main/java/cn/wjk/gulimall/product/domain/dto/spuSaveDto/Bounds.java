package cn.wjk.gulimall.product.domain.dto.spuSaveDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Bounds {
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}