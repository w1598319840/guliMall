package cn.wjk.gulimall.common.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPriceDTO {
    private Long id;
    private String name;
    private BigDecimal price;
}