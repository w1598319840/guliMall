package cn.wjk.gulimall.coupon.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.coupon.entity.SpuBoundsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:34:15
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

