package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.dao.BrandDao;
import cn.wjk.gulimall.product.domain.entity.BrandEntity;
import cn.wjk.gulimall.product.domain.entity.CategoryBrandRelationEntity;
import cn.wjk.gulimall.product.service.BrandService;
import cn.wjk.gulimall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service("brandService")
@RequiredArgsConstructor
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    private final CategoryBrandRelationService categoryBrandRelationservice;

    @Override
    public PageUtils queryPage(PageDTO pageDTO) {
        String key = pageDTO.getKey();
        Page<BrandEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        lambdaQuery()
                .eq(key != null, BrandEntity::getBrandId, key)
                .or()
                .like(key != null, BrandEntity::getName, key)
                .page(page);

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        if (brand.getBrandId() == null) {
            return;
        }
        updateById(brand);
        //必须保证逻辑外键的冗余字段的数据一致性
        //pms_category_brand_relation表
        categoryBrandRelationservice.lambdaUpdate()
                .eq(CategoryBrandRelationEntity::getBrandId, brand.getBrandId())
                .set(brand.getName() != null, CategoryBrandRelationEntity::getBrandName, brand.getName())
                .update();
    }
}