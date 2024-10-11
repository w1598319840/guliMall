package cn.wjk.gulimall.ware.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.ware.domain.dto.WareMergeDTO;
import cn.wjk.gulimall.ware.domain.entity.PurchaseEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 采购信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询新建、已领取状态的采购单
     */
    PageUtils getUnreceive();

    /**
     * 合并采购单
     */
    void merge(WareMergeDTO wareMergeDTO);
}

