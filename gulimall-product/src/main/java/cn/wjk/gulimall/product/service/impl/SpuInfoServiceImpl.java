package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.constant.ProductConstant;
import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import cn.wjk.gulimall.common.domain.to.SpuBoundsTO;
import cn.wjk.gulimall.common.domain.to.es.AttrEsModel;
import cn.wjk.gulimall.common.domain.to.es.SkuEsModel;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.RPCException;
import cn.wjk.gulimall.common.feign.CouponFeign;
import cn.wjk.gulimall.common.feign.SearchFeign;
import cn.wjk.gulimall.common.feign.WareFeign;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.dao.*;
import cn.wjk.gulimall.product.domain.dto.spuSaveDto.*;
import cn.wjk.gulimall.product.domain.entity.*;
import cn.wjk.gulimall.product.domain.vo.SpuInfoVO;
import cn.wjk.gulimall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final BrandDao brandDao;
    private final CategoryDao categoryDao;
    private final CouponFeign couponFeign;
    private final WareFeign wareFeign;
    private final SearchFeign searchFeign;

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

    @Override
    public PageUtils queryPageByCondition(PageDTO pageDTO) {
        if (pageDTO == null) {
            return PageUtils.emptyPageUtils();
        }
        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        Page<SpuInfoEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("id", key)
                        .or()
                        .like("spu_name", key)
                        .or()
                        .like("spu_description", key)
        );
        Long catelogId = pageDTO.getCatelogId();
        Long brandId = pageDTO.getBrandId();
        queryWrapper.eq(catelogId != null && !catelogId.equals(0L), "catalog_id", catelogId);
        queryWrapper.eq(brandId != null && !brandId.equals(0L), "brand_id", brandId);
        queryWrapper.eq(pageDTO.getStatus() != null, "publish_status", pageDTO.getStatus());
        page(page, queryWrapper);

        //将Entity转化为VO
        List<SpuInfoEntity> spuInfoEntityList = page.getRecords();
        if (spuInfoEntityList.isEmpty()) {
            return PageUtils.emptyPageUtils();
        }
        Set<Long> brandIds = spuInfoEntityList.stream().map(SpuInfoEntity::getBrandId).collect(Collectors.toSet());
        Set<Long> catalogIds = spuInfoEntityList.stream().map(SpuInfoEntity::getCatalogId).collect(Collectors.toSet());
        Map<Long, String> brandIdToBrandNameMap
                = brandDao.selectBatchIds(brandIds)
                .stream().collect(Collectors.toMap(BrandEntity::getBrandId, BrandEntity::getName));
        Map<Long, String> catalogIdToCatalogNameMap
                = categoryDao.selectBatchIds(catalogIds)
                .stream().collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));

        List<SpuInfoVO> spuInfoVOList = spuInfoEntityList.stream().map(spuInfoEntity -> {
            SpuInfoVO spuInfoVO = new SpuInfoVO();
            BeanUtils.copyProperties(spuInfoEntity, spuInfoVO);
            spuInfoVO.setBrandName(brandIdToBrandNameMap.get(spuInfoEntity.getBrandId()));
            spuInfoVO.setCatalogName(catalogIdToCatalogNameMap.get(spuInfoEntity.getCatalogId()));
            return spuInfoVO;
        }).toList();

        return new PageUtils(page, spuInfoVOList);
    }

    @Override
    public void up(Long spuId) {
        //1. 封装数据
        //sku_info表
        List<SkuInfoEntity> skuInfoEntities
                = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).toList();

        //brand表
        Map<Long, Long> skuIdToBrandIdMap = skuInfoEntities.stream().collect(Collectors.toMap(
                SkuInfoEntity::getSkuId,
                SkuInfoEntity::getBrandId));
        Map<Long, BrandEntity> brandIdToBrandEntityMap =
                brandDao.selectBatchIds(skuIdToBrandIdMap.values().stream().distinct().toList())
                        .stream().collect(Collectors.toMap(BrandEntity::getBrandId, brandEntity -> brandEntity));

        //category表
        Map<Long, Long> skuIdToCategoryIdMap = skuInfoEntities.stream().collect(Collectors.toMap(
                SkuInfoEntity::getSkuId,
                SkuInfoEntity::getCatalogId
        ));
        Map<Long, CategoryEntity> categoryIdToCategoryEntityMap =
                categoryDao.selectBatchIds(skuIdToCategoryIdMap.values().stream().distinct().toList())
                        .stream().collect(Collectors.toMap(CategoryEntity::getCatId, categoryEntity -> categoryEntity));

        //attrs，并且要是能够检索的(数据库表中有字段标识了是否能够被检索)
        //实际上同一个spu下的sku的attr应该是一样的
        //我的表中数据不一样纯是因为当时插入数据时前端页面有BUG，我无法使用，因此直接使用http client插入了一些不符合标准的测试数据
        Map<Long, ProductAttrValueEntity> attrIdToProductAttrValueEntityMap =
                productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId))
                        .stream().collect(Collectors.toMap(ProductAttrValueEntity::getAttrId, p -> p));
        List<Long> searchAttrIds = attrDao.selectList(new QueryWrapper<AttrEntity>()
                        .in("attr_id", attrIdToProductAttrValueEntityMap.keySet()))
                .stream().filter(attrEntity -> attrEntity.getSearchType().equals(1))
                .map(AttrEntity::getAttrId).toList();
        List<AttrEsModel> attrs = searchAttrIds.stream().map(searchAttrId -> {
            ProductAttrValueEntity productAttrValueEntity = attrIdToProductAttrValueEntityMap.get(searchAttrId);
            AttrEsModel attrEsModel = new AttrEsModel();
            BeanUtils.copyProperties(productAttrValueEntity, attrEsModel);
            return attrEsModel;
        }).toList();
//        Map<Long, List<SkuSaleAttrValueEntity>> skuIdToSkuSaleAttrValueEntitiesMap =
//                skuSaleAttrValueDao.selectList(new QueryWrapper<SkuSaleAttrValueEntity>().in("sku_id", skuIds))
//                        .stream().collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getSkuId));

        //远程调用来获取sku的库存数量
        R result = wareFeign.getSkuStock(skuIds);
        Object o = result.get("data");
        if (result.getCode() != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        Object data = result.get("data");
        if (!(data instanceof Map<?, ?>)) {
            throw new RPCException(BizHttpStatusEnum.RPC_DATA_EXCEPTION);
        }
        @SuppressWarnings("unchecked")
        Map<String, Integer> skuIdToStockMapRaw = (Map<String, Integer>) o;
        Map<Long, Long> skuIdToStockMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : skuIdToStockMapRaw.entrySet()) {
            //将byte、int -> long
            String key = entry.getKey();
            long value = entry.getValue();
            skuIdToStockMap.put(Long.valueOf(key), value);
        }

        //广快封装
        List<SkuEsModel> skuEsModels = skuInfoEntities.stream().map(skuInfo -> {
            Long skuId = skuInfo.getSkuId();
            Long brandId = skuIdToBrandIdMap.get(skuId);
            BrandEntity brandEntity = brandIdToBrandEntityMap.get(brandId);
            Long categoryId = skuIdToCategoryIdMap.get(skuId);
            CategoryEntity categoryEntity = categoryIdToCategoryEntityMap.get(categoryId);
//            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuIdToSkuSaleAttrValueEntitiesMap.get(skuId);
//            List<AttrEsModel> attrs = skuSaleAttrValueEntities.stream().map(skuSaleAttrValue -> {
//                AttrEsModel attrEsModel = new AttrEsModel();
//                attrEsModel.setAttrId(skuSaleAttrValue.getAttrId());
//                attrEsModel.setAttrName(skuSaleAttrValue.getAttrName());
//                attrEsModel.setAttrValue(skuSaleAttrValue.getAttrValue());
//                return attrEsModel;
//            }).toList();
            SkuEsModel skuEsModel = new SkuEsModel();
            skuEsModel.setSkuId(skuId);
            skuEsModel.setSpuId(spuId);
            skuEsModel.setSkuTitle(skuInfo.getSkuTitle());
            skuEsModel.setSkuPrice(skuInfo.getPrice());
            skuEsModel.setSkuImg(skuInfo.getSkuDefaultImg());
            skuEsModel.setSaleCount(skuInfo.getSaleCount());
            skuEsModel.setHasStock(!skuIdToStockMap.get(skuId).equals(0L));//是否有库存
            skuEsModel.setHotScore(0L);//热点点数，现在刚上架赋为0，实际场景中会有各种操作，加钱热度就高
            skuEsModel.setBrandId(brandId);
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            skuEsModel.setCatalogId(categoryId);
            skuEsModel.setCatalogName(categoryEntity.getName());
            skuEsModel.setAttrs(attrs);
            return skuEsModel;
        }).toList();

        //2. 远程调用，将商品数据插入ES中
        R upResult = searchFeign.up(skuEsModels);
        if (upResult.getCode() != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }

        //3. 修改商品的当前状态
        this.lambdaUpdate()
                .set(SpuInfoEntity::getPublishStatus, ProductConstant.SpuStatus.UP.getCode())
                .set(SpuInfoEntity::getUpdateTime, new Date())
                .eq(SpuInfoEntity::getId, spuId)
                .update();
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
            }).filter(skuImagesEntity -> StringUtils.isNotBlank(skuImagesEntity.getImgUrl())).toList();
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