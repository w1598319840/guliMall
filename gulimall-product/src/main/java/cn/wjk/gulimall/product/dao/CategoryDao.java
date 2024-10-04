package cn.wjk.gulimall.product.dao;

import cn.wjk.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
