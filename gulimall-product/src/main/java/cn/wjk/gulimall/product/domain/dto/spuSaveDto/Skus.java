package cn.wjk.gulimall.product.domain.dto.spuSaveDto;

import cn.wjk.gulimall.common.domain.dto.MemberPriceDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Skus {
    private Long skuId;
    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceDTO> memberPrice;
}