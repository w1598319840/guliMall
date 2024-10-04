package cn.wjk.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:54:17
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

