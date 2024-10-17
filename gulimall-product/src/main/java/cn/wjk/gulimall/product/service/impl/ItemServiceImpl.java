package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.product.dao.*;
import cn.wjk.gulimall.product.domain.entity.SkuImagesEntity;
import cn.wjk.gulimall.product.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.product.domain.entity.SkuSaleAttrValueEntity;
import cn.wjk.gulimall.product.domain.vo.SkuItemVO;
import cn.wjk.gulimall.product.service.ItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Package: cn.wjk.gulimall.product.service.impl
 * @ClassName: ItemServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/17 下午8:37
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final SkuInfoDao skuInfoDao;
    private final SkuImagesDao skuImagesDao;
    private final SpuInfoDescDao spuInfoDescDao;
    private final SkuSaleAttrValueDao skuSaleAttrValueDao;
    private final AttrGroupDao attrGroupDao;

    @Override
    public SkuItemVO getItemDetail(Long skuId) {
        if (skuId == null) {
            return null;
        }
        SkuItemVO skuItemVO = new SkuItemVO();
        SkuInfoEntity skuInfoEntity = skuInfoDao.selectById(skuId);
        if (skuInfoEntity == null) {
            return null;
        }
        skuItemVO.setInfo(skuInfoEntity);
        skuItemVO.setImages(skuImagesDao.selectList(new QueryWrapper<SkuImagesEntity>()
                .eq("sku_id", skuId)));
        Long spuId = skuInfoEntity.getSpuId();
        skuItemVO.setDesp(spuInfoDescDao.selectById(spuId));
        skuItemVO.setGroupAttrs(attrGroupDao.selectGroupAttrsWithSpuIdAndCatalogId(spuId, skuInfoEntity.getCatalogId()));
        skuItemVO.setSaleAttr(getSaleAttr(spuId));
        return skuItemVO;
    }

    /**
     * 获取商品详细中的attr信息
     */
    private List<SkuItemVO.ItemSaleAttrVO> getSaleAttr(Long spuId) {
        //1.根据spuId查询出其旗下所有的sku
        List<SkuInfoEntity> skuInfoEntities = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>()
                .eq("spu_id", spuId));

        if (skuInfoEntities == null || skuInfoEntities.isEmpty()) {
            return Collections.emptyList();
        }
        //2.查询出这些sku所有的attrVO
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).toList();
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueDao
                .selectList(new QueryWrapper<SkuSaleAttrValueEntity>().in("sku_id", skuIds));
        Map<Long, String> attrIdToAttrNameMap = skuSaleAttrValueEntities.stream()
                .collect(Collectors.toMap(SkuSaleAttrValueEntity::getAttrId, SkuSaleAttrValueEntity::getAttrName,
                        //如果出现重复的key，如何处理
                        (existing, replacement) -> existing));
        Map<Long, List<String>> attrIdToAttrValuesMap =
                skuSaleAttrValueEntities.stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId,
                        Collectors.mapping(SkuSaleAttrValueEntity::getAttrValue, Collectors.toList())));
        return attrIdToAttrNameMap.entrySet().stream().map(entry -> {
            SkuItemVO.ItemSaleAttrVO itemSaleAttrVO = new SkuItemVO.ItemSaleAttrVO();
            Long attrId = entry.getKey();
            String attrName = entry.getValue();
            itemSaleAttrVO.setAttrId(attrId);
            itemSaleAttrVO.setAttrName(attrName);
            itemSaleAttrVO.setAttrValues(attrIdToAttrValuesMap.get(attrId).stream().distinct().toList());
            return itemSaleAttrVO;
        }).toList();
    }
}
