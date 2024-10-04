package cn.wjk.gulimall.coupon.dao;

import cn.wjk.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:34:14
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
