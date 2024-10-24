package cn.wjk.gulimall.product.dao;

import cn.wjk.gulimall.product.domain.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    /**
     * 根据skuId获取该sku的属性，以List的方式返回，每个属性以及其值拼接成一个String
     */
    List<String> getAttrWithStringBySkuId(@Param("skuId") Long skuId);
}
