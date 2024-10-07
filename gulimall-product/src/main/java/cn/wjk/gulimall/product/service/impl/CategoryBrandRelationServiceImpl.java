package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.BrandDao;
import cn.wjk.gulimall.product.dao.CategoryBrandRelationDao;
import cn.wjk.gulimall.product.domain.dto.CategoryBrandRelationDTO;
import cn.wjk.gulimall.product.domain.entity.CategoryBrandRelationEntity;
import cn.wjk.gulimall.product.service.CategoryBrandRelationService;
import cn.wjk.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Resource
    private CategoryService categoryService;
    @Resource
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> getRelatedCategoryByBrandId(Long brandId) {
        if (brandId == null) {
            return Collections.emptyList();
        }
        return lambdaQuery().eq(CategoryBrandRelationEntity::getBrandId, brandId).list();
    }

    @Override
    @Transactional
    public void saveDetail(CategoryBrandRelationDTO categoryBrandRelationDTO) {
        if (categoryBrandRelationDTO == null ||
                categoryBrandRelationDTO.getBrandId() == null ||
                categoryBrandRelationDTO.getCatelogId() == null) {
            return;
        }
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        BeanUtils.copyProperties(categoryBrandRelationDTO, entity);
        entity.setBrandName(brandDao.selectById(entity.getBrandId()).getName());
        entity.setCatelogName(categoryService.getById(entity.getCatelogId()).getName());
        save(entity);
    }
}