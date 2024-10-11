package cn.wjk.gulimall.ware.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.ware.domain.dto
 * @ClassName: PurchaseItemDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午7:05
 * @Description: 完成采购时每一个采购项的DTO
 */
@Data
public class PurchaseItemDTO {
    /**
     * purchaseDetailId
     */
    private Long itemId;
    private Integer status;
    /**
     * 采购失败的原因
     */
    private String reason;
}
