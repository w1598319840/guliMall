package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import cn.wjk.gulimall.common.domain.to.SpuBoundsTO;
import cn.wjk.gulimall.common.feign.CouponFeign;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.dao.*;
import cn.wjk.gulimall.product.domain.dto.spuSaveDto.*;
import cn.wjk.gulimall.product.domain.entity.*;
import cn.wjk.gulimall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
@RequiredArgsConstructor
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuInfoDescDao spuInfoDescDao;
    private final SpuImagesDao spuImagesDao;
    private final ProductAttrValueDao productAttrValueDao;
    private final AttrDao attrDao;
    private final SkuInfoDao skuInfoDao;
    private final SkuImagesDao skuImagesDao;
    private final SkuSaleAttrValueDao skuSaleAttrValueDao;
    private final CouponFeign couponFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveDTO spuSaveDTO) {
        if (spuSaveDTO == null) {
            return;
        }

        //1. 保存spu基本信息 pms_spu_info
        saveSpuBaseInfo(spuSaveDTO);
        //该步后spuSaveDTO中已有该spu的id信息

        //2. 保存spu的描述图片 pms_spu_info_desc
        saveSpuInfoDesc(spuSaveDTO);

        //3. 保存spu的图片集 pms_spu_images
        saveSpuImages(spuSaveDTO);

        //4. 保存spu的规格参数 pms_product_attr_value
        saveProductAttrValue(spuSaveDTO);

        //5. 保存spu的积分信息 gulimall_sms -> sms_spu_bounds
        saveSpuBounds(spuSaveDTO);

        //5. 保存当前spu对应的所有sku信息
        saveSku(spuSaveDTO);
    }

    /**
     * 保存当前spu对应的所有sku信息
     */
    private void saveSku(SpuSaveDTO spuSaveDTO) {
        //1. sku基本信息 pms_sku_info
        saveSkuInfo(spuSaveDTO);
        //完成该步后，skus中的每个sku的skuId字段已经填充完毕

        //2. sku图片信息 pms_sku_images
        saveSkuImages(spuSaveDTO);

        //3. sku销售属性信息 pms_sku_sale_attr_value
        saveSkuSaleAttrValue(spuSaveDTO);

        //4. sku优惠、满减信息 gulimall_sms
        //                    -> sms_sku_ladder/sms_sku_full_reduction/sms_member_price
        saveSkuReduction(spuSaveDTO);
    }

    /**
     * sku优惠、满减信息
     */
    private void saveSkuReduction(SpuSaveDTO spuSaveDTO) {
        List<SkuReductionTO> skuReductionTOs = spuSaveDTO.getSkus().stream().map(sku -> {
            SkuReductionTO skuReductionTO = new SkuReductionTO();
            BeanUtils.copyProperties(sku, skuReductionTO);
            return skuReductionTO;
        }).toList();
        R result = couponFeign.saveSkuReduction(skuReductionTOs);
        int code = result.getCode();
        if (code != 0) {
            log.error("远程保存sku优惠、满减信息失败，当前状态码为:{}", code);
        }
    }

    /**
     * sku销售属性信息
     */
    private void saveSkuSaleAttrValue(SpuSaveDTO spuSaveDTO) {
        List<Skus> skus = spuSaveDTO.getSkus();
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = new ArrayList<>();
        for (Skus sku : skus) {
            Long skuId = sku.getSkuId();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = sku.getAttr().stream().map(attr -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                skuSaleAttrValueEntity.setSkuId(skuId);
                skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                return skuSaleAttrValueEntity;
            }).toList();
            skuSaleAttrValueEntities.addAll(skuSaleAttrValueEntityList);
        }
        skuSaleAttrValueDao.insert(skuSaleAttrValueEntities);
    }

    /**
     * sku图片信息
     */
    private void saveSkuImages(SpuSaveDTO spuSaveDTO) {
        List<Skus> skus = spuSaveDTO.getSkus();
        List<SkuImagesEntity> skuImagesEntities = new ArrayList<>();
        for (Skus sku : skus) {
            Long skuId = sku.getSkuId();
            List<SkuImagesEntity> skuImagesEntityList = sku.getImages().stream().map(image -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        skuImagesEntity.setSkuId(skuId);
                        skuImagesEntity.setImgUrl(image.getImgUrl());
                        skuImagesEntity.setDefaultImg(image.getDefaultImg() == 1 ? 1 : 0);
                        return skuImagesEntity;
                    }).filter(skuImagesEntity -> skuImagesEntity.getImgUrl() != null)
                    .toList();
            skuImagesEntities.addAll(skuImagesEntityList);
        }
        //批量保存
        skuImagesDao.insert(skuImagesEntities);
    }

    /**
     * sku基本信息
     */
    private void saveSkuInfo(SpuSaveDTO spuSaveDTO) {
        List<Skus> skus = spuSaveDTO.getSkus();
        Long spuId = spuSaveDTO.getSpuId();
        Long catalogId = spuSaveDTO.getCatalogId();
        Long brandId = spuSaveDTO.getBrandId();
        List<SkuInfoEntity> skuInfoEntityList = skus.stream().map(sku -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setCatalogId(catalogId);
            skuInfoEntity.setBrandId(brandId);
            List<Images> images = sku.getImages().stream().filter(image -> image.getDefaultImg() == 1).toList();
            if (!images.isEmpty()) {
                skuInfoEntity.setSkuDefaultImg(images.getFirst().getImgUrl());
            }
            skuInfoEntity.setSaleCount(0L);
            return skuInfoEntity;
        }).toList();
        skuInfoDao.insert(skuInfoEntityList);
        for (int i = 0; i < skus.size(); i++) {
            skus.get(i).setSkuId(skuInfoEntityList.get(i).getSkuId());
        }
    }

    /**
     * 保存spu的积分信息
     */
    private void saveSpuBounds(SpuSaveDTO spuSaveDTO) {
        //对应的表在sms中
        Bounds bounds = spuSaveDTO.getBounds();
        R result = couponFeign.saveSpuBounds(
                new SpuBoundsTO(spuSaveDTO.getSpuId(), bounds.getBuyBounds(), bounds.getGrowBounds())
        );
        int code = result.getCode();
        if (code != 0) {
            log.error("远程保存spu的积分信息失败，当前状态码为:{}", code);
        }
    }

    /**
     * 保存spu的规格参数
     */
    private void saveProductAttrValue(SpuSaveDTO spuSaveDTO) {
        Long spuId = spuSaveDTO.getSpuId();
        //查询出所有attrId和attrName的映射关系
        List<BaseAttrs> attrs = spuSaveDTO.getBaseAttrs();
        List<Long> attrIds = attrs.stream().map(BaseAttrs::getAttrId).toList();
        Map<Long, String> attrIdToAttrNameMap
                = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds))
                .stream().collect(Collectors.toMap(
                        AttrEntity::getAttrId,
                        AttrEntity::getAttrName
                ));

        //构建并插入数据
        List<ProductAttrValueEntity> prodcutAttrValueEntityList
                = attrs.stream().map(baseAttr -> {
            Long attrId = baseAttr.getAttrId();
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuId);
            productAttrValueEntity.setAttrId(attrId);
            productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
            productAttrValueEntity.setAttrName(attrIdToAttrNameMap.get(attrId));
            return productAttrValueEntity;
        }).toList();

        productAttrValueDao.insert(prodcutAttrValueEntityList);
    }

    /**
     * 保存spu的图片集
     */
    private void saveSpuImages(SpuSaveDTO spuSaveDTO) {
        Long id = spuSaveDTO.getSpuId();
        List<SpuImagesEntity> spuImagesEntityList = spuSaveDTO.getImages().stream().map(image -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(id);
            spuImagesEntity.setImgUrl(image);
            return spuImagesEntity;
        }).toList();
        spuImagesDao.insert(spuImagesEntityList);
    }

    /**
     * 保存spu的描述图片
     */
    private void saveSpuInfoDesc(SpuSaveDTO spuSaveDTO) {
        String description = String.join(",", spuSaveDTO.getDecript());
        spuInfoDescDao.insert(new SpuInfoDescEntity(spuSaveDTO.getSpuId(), description));
    }

    /**
     * 保存spu的基本信息
     */
    private void saveSpuBaseInfo(SpuSaveDTO spuSaveDTO) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveDTO, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        spuSaveDTO.setSpuId(spuInfoEntity.getId());
    }
}