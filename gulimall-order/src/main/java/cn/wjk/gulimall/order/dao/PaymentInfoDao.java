package cn.wjk.gulimall.order.dao;

import cn.wjk.gulimall.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:54:17
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
