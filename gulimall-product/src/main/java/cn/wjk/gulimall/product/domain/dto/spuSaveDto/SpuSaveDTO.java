package cn.wjk.gulimall.product.domain.dto.spuSaveDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpuSaveDTO {
    private Long spuId;
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private Integer publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}