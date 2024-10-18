package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.product.dao.*;
import cn.wjk.gulimall.product.domain.entity.SkuImagesEntity;
import cn.wjk.gulimall.product.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.product.domain.entity.SkuSaleAttrValueEntity;
import cn.wjk.gulimall.product.domain.vo.SkuItemVO;
import cn.wjk.gulimall.product.service.ItemService;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
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
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final SkuInfoDao skuInfoDao;
    private final SkuImagesDao skuImagesDao;
    private final SpuInfoDescDao spuInfoDescDao;
    private final SkuSaleAttrValueDao skuSaleAttrValueDao;
    private final AttrGroupDao attrGroupDao;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Override
    public SkuItemVO getItemDetail(Long skuId) {
        if (skuId == null) {
            return null;
        }
        SkuItemVO skuItemVO = new SkuItemVO();
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = skuInfoDao.selectById(skuId);
            if (skuInfoEntity == null) {
                return null;
            }
            skuItemVO.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() ->
                skuItemVO.setImages(skuImagesDao.selectList(new QueryWrapper<SkuImagesEntity>()
                        .eq("sku_id", skuId))), threadPoolExecutor);
        //以下任务都需要任务1的完成，等待任务1的完成
        SkuInfoEntity skuInfoEntity = null;
        try {
            skuInfoEntity = infoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        if (skuInfoEntity == null) {
            return null;
        }
        Long spuId = skuInfoEntity.getSpuId();
        CompletableFuture<Void> despFuture = CompletableFuture.runAsync(() ->
                skuItemVO.setDesp(spuInfoDescDao.selectById(spuId)), threadPoolExecutor);
        SkuInfoEntity finalSkuInfoEntity = skuInfoEntity;
        CompletableFuture<Void> groupAttrsFuture = CompletableFuture.runAsync(() ->
                skuItemVO.setGroupAttrs(attrGroupDao.selectGroupAttrsWithSpuIdAndCatalogId(spuId, finalSkuInfoEntity.getCatalogId())), threadPoolExecutor);
        CompletableFuture<Void> saleAttrFuture = CompletableFuture.runAsync(() ->
                skuItemVO.setSaleAttr(getSaleAttr(spuId)), threadPoolExecutor);
        CompletableFuture.allOf(infoFuture, imageFuture, despFuture, groupAttrsFuture, saleAttrFuture).join();
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
        Map<Long, List<SkuSaleAttrValueEntity>> attrIdToSkuSalAttrValueEntityMap =
                skuSaleAttrValueEntities.stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId));
        return attrIdToAttrNameMap.entrySet().stream().map(entry -> {
            SkuItemVO.ItemSaleAttrVO itemSaleAttrVO = new SkuItemVO.ItemSaleAttrVO();
            Long attrId = entry.getKey();
            String attrName = entry.getValue();
            itemSaleAttrVO.setAttrId(attrId);
            itemSaleAttrVO.setAttrName(attrName);
            //将某一个属性的下所有的entity取出，并构建成List<AttrValueWithSkuIdVO>
            Map<String, List<Long>> attrValueToSkuIdsMap =
                    attrIdToSkuSalAttrValueEntityMap.get(attrId).stream().collect(Collectors.groupingBy(
                            SkuSaleAttrValueEntity::getAttrValue, Collectors.mapping(
                                    SkuSaleAttrValueEntity::getSkuId, Collectors.toList())));
            itemSaleAttrVO.setAttrValues(attrValueToSkuIdsMap.entrySet().stream().map(e -> {
                List<Long> skuIdList = e.getValue();
                SkuItemVO.AttrValueWithSkuIdVO vo = new SkuItemVO.AttrValueWithSkuIdVO();
                vo.setAttrValue(e.getKey());
                vo.setSkuIds(StringUtils.join(skuIdList, ","));
                return vo;
            }).toList());
            return itemSaleAttrVO;
        }).toList();
    }
}
