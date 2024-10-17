package cn.wjk.gulimall.product.dao;

import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.domain.vo.SkuItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * 根据参数规格id批量查询其相关联的分组的详细信息
     *
     * @param attrIds 参数规格id的集合
     * @return 对应的分组信息的集合
     */
    List<AttrGroupEntity> selectGroupByAttrId(List<Long> attrIds);

    /**
     * 根据spuId和catalogId查询对应所有group以及其下的attr及attrValue
     */
    List<SkuItemVO.SpuItemAttrGroupVO> selectGroupAttrsWithSpuIdAndCatalogId(@Param("spuId") Long spuId,
                                                                             @Param("catalogId") Long catalogId);
}
