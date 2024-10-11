package cn.wjk.gulimall.ware.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询采购需求
     */
    PageUtils pageQueryDetail(PageDTO pageDTO);
}

