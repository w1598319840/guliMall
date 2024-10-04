package cn.wjk.gulimall.order.dao;

import cn.wjk.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:54:17
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
