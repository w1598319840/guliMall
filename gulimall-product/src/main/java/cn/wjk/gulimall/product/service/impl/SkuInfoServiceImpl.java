package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.SkuInfoDao;
import cn.wjk.gulimall.product.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils pageQuerySku(PageDTO pageDTO) {
        if (pageDTO == null) {
            return PageUtils.emptyPageUtils();
        }

        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        Page<SkuInfoEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(key != null, wrapper ->
                wrapper.eq("sku_id", key)
                        .or()
                        .like("sku_name", key)
                        .or()
                        .like("sku_desc", key)
                        .or()
                        .like("sku_title", key)
                        .or()
                        .like("sku_subtitle", key)
        );
        Long catelogId = pageDTO.getCatelogId();
        Long brandId = pageDTO.getBrandId();
        BigDecimal min = pageDTO.getMin();
        BigDecimal max = pageDTO.getMax();
        queryWrapper.eq(catelogId != null && !catelogId.equals(0L), "catalog_id", catelogId);
        queryWrapper.eq(brandId != null && !brandId.equals(0L), "brand_id", brandId);
        queryWrapper.ge(min != null && min.compareTo(BigDecimal.ZERO) > 0, "price", min);
        queryWrapper.le(max != null && max.compareTo(BigDecimal.ZERO) > 0, "price", max);
        page(page, queryWrapper);

        return new PageUtils(page);
    }

}