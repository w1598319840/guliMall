package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.CategoryBrandRelationDao;
import cn.wjk.gulimall.product.domain.entity.CategoryBrandRelationEntity;
import cn.wjk.gulimall.product.domain.vo.CategoryBrandRelationVO;
import cn.wjk.gulimall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationVO> getRelatedCategoryByBrandId(Long brandId) {
        if (brandId == null) {
            return null;
        }
        List<CategoryBrandRelationEntity> cbr = lambdaQuery().eq(CategoryBrandRelationEntity::getBrandId, brandId).list();
        return cbr.stream().map(categoryBrandRelationEntity -> {
            CategoryBrandRelationVO categoryBrandRelationVO = new CategoryBrandRelationVO();
            BeanUtils.copyProperties(categoryBrandRelationEntity, categoryBrandRelationVO);
            return categoryBrandRelationVO;
        }).toList();
    }

}