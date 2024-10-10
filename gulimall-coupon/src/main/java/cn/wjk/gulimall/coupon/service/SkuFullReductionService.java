package cn.wjk.gulimall.coupon.service;

import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品满减信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:34:15
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存sku满减信息
     */
    void saveSkuReduction(List<SkuReductionTO> skuReductionTOs);
}

