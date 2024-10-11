package cn.wjk.gulimall.ware.dao;

import cn.wjk.gulimall.ware.domain.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 商品库存
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    /**
     * 更新库存
     */
    @Update("""
            update wms_ware_sku set stock = stock + #{stock} where sku_id = #{skuId} and ware_id = #{wareId}
            """)
    void updateWareSkuStock(WareSkuEntity wareSkuEntity);
}
