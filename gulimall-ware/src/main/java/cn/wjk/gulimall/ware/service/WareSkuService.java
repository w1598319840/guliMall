package cn.wjk.gulimall.ware.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.ware.domain.entity.WareSkuEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页查询
     */
    PageUtils pageQueryDetail(PageDTO pageDTO);

    /**
     * 获取sku库存数量
     */
    Map<Long, Long> getSkuStock(List<Long> skuIds);
}

