package cn.wjk.gulimall.ware.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.ware.domain.dto
 * @ClassName: PurchaseDoneDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午7:04
 * @Description: 完成采购的DTO
 */
@Data
public class PurchaseDoneDTO {
    /**
     * 采购单(purchase)id
     */
    private Long id;
    private List<PurchaseItemDTO> items;
}
