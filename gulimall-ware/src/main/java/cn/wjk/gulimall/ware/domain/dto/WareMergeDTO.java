package cn.wjk.gulimall.ware.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.ware.domain.dto
 * @ClassName: WareMergeDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午2:57
 * @Description: 合并采购单时的DTO
 */
@Data
public class WareMergeDTO {
    private Long purchaseId;
    private List<Long> items;
}
