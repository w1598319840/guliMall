package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.CategoryBrandRelationDao;
import cn.wjk.gulimall.product.dao.CategoryDao;
import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import cn.wjk.gulimall.product.domain.vo.CategoryVO;
import cn.wjk.gulimall.product.domain.vo.Catelog2VO;
import cn.wjk.gulimall.product.domain.vo.Catelog3VO;
import cn.wjk.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryVO> listWithTree() {
        List<CategoryEntity> categoryEntities = lambdaQuery().list();
        List<CategoryVO> categoryVOs = categoryEntities.stream().map(categoryEntity -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(categoryEntity, categoryVO);
            return categoryVO;
        }).toList();

        //将所有的分类组装成树形结构返回
        //key: level value: 对应的分类
        Comparator<CategoryVO> comparator = (c1, c2) -> {
            Integer s1 = c1.getSort();
            Integer s2 = c2.getSort();
            return (s1 == null ? 0 : s1) - (s2 == null ? 0 : s2);
        };
        //根据parentId分组
        Map<Long, List<CategoryVO>> parentIdMap
                = categoryVOs.stream()
                .sorted(comparator)
                .collect(Collectors.groupingBy(CategoryVO::getParentCid));
        //遍历每一个category，然后将与该categoryId相匹配的parentId的list加入到当前category的children中
        for (CategoryVO categoryVO : categoryVOs) {
            List<CategoryVO> children = parentIdMap.get(categoryVO.getCatId());
            if (children == null) {
                continue;
            }
            categoryVO.setChildren(children.stream()
                    .sorted(comparator)
                    .toList());
        }
        return categoryVOs.stream().filter(categoryVO -> categoryVO.getCatLevel().equals(1))
                .sorted(comparator)
                .toList();
    }

    @Override
    public void removeCategoryByIds(List<Long> ids) {
        //使用逻辑删除
        categoryDao.deleteByIds(ids);
    }

    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        if (category.getCatId() == null) {
            return;
        }
        updateById(category);
        //级联更新
        //pms_category_brand_relation表
        categoryBrandRelationDao.updateNameById(category.getName(), category.getCatId());
    }

    @Override
    public Long[] getCatelogPathById(Long catelogId) {
        ArrayList<Long> list = new ArrayList<>();
        list.addFirst(catelogId);
        long currentCatelogId = catelogId;
        do {
            CategoryEntity category = lambdaQuery()
                    .eq(CategoryEntity::getCatId, currentCatelogId)
                    .one();
            if (category.getCatLevel() == 1) {
                break;
            }
            currentCatelogId = category.getParentCid();
            list.addFirst(currentCatelogId);
        } while (true);
        return list.toArray(new Long[0]);
    }

    @Override
    public List<CategoryEntity> getAllRootCategories() {
        return lambdaQuery().eq(CategoryEntity::getCatLevel, 1)
                .list();
    }

    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() {
        List<CategoryEntity> allCategories = this.lambdaQuery().list();
        Map<Integer, List<CategoryEntity>> catLevelToCategoryMap =
                allCategories.stream().collect(Collectors.groupingBy(CategoryEntity::getCatLevel));
        HashMap<String, List<Catelog2VO>> result = new HashMap<>();
        List<CategoryEntity> firstLevelCategories = catLevelToCategoryMap.get(1);
        List<CategoryEntity> secondLevelCategories = catLevelToCategoryMap.get(2);
        List<CategoryEntity> thirdLevelCategories = catLevelToCategoryMap.get(3);
        for (CategoryEntity firstLevelCategory : firstLevelCategories) {
            result.put(firstLevelCategory.getCatId().toString(), new ArrayList<>());
        }
        Map<Long, List<Catelog3VO>> thirdsecondLevelCategoryParentIdToCatelog3VOMap =
                thirdLevelCategories.stream().collect(Collectors.groupingBy(CategoryEntity::getParentCid,
                        //对value再进行一次操作
                        Collectors.mapping(categoryEntity -> {
                            Catelog3VO catelog3VO = new Catelog3VO();
                            catelog3VO.setName(categoryEntity.getName());
                            catelog3VO.setId(categoryEntity.getCatId().toString());
                            catelog3VO.setCatalog2Id(categoryEntity.getParentCid().toString());
                            return catelog3VO;
                        }, Collectors.toList())));
        for (CategoryEntity secondLevelCategory : secondLevelCategories) {
            Long catId = secondLevelCategory.getCatId();
            Catelog2VO catelog2VO = new Catelog2VO();
            catelog2VO.setCatalog1Id(secondLevelCategory.getParentCid().toString());
            catelog2VO.setName(secondLevelCategory.getName());
            catelog2VO.setId(catId.toString());
            catelog2VO.setCatalog3List(thirdsecondLevelCategoryParentIdToCatelog3VOMap.get(catId));
            result.get(secondLevelCategory.getParentCid().toString()).add(catelog2VO);
        }
        return result;
    }


    /**
     * 效率太低
     */
    @Deprecated
    public Map<String, List<Catelog2VO>> getCatalogJsonDeprecated() {
        List<CategoryVO> firstLevelCat = listWithTree();
        Map<Long, List<CategoryVO>> firstLevelCatIdToSecondLevelCatMap =
                firstLevelCat.stream().collect(Collectors.toMap(CategoryVO::getCatId, CategoryVO::getChildren));
        Map<String, List<Catelog2VO>> result = new HashMap<>();
        for (Map.Entry<Long, List<CategoryVO>> entry : firstLevelCatIdToSecondLevelCatMap.entrySet()) {
            String firstLevelCatIdString = entry.getKey().toString();
            List<Catelog2VO> catelog2VOs = entry.getValue().stream().map(secondLevelCat -> {
                Catelog2VO catelog2VO = new Catelog2VO();
                catelog2VO.setId(secondLevelCat.getCatId().toString());
                catelog2VO.setName(secondLevelCat.getName());
                catelog2VO.setCatalog1Id(firstLevelCatIdString);
                List<CategoryVO> thirdLevelCats = secondLevelCat.getChildren();
                if (thirdLevelCats != null) {
                    catelog2VO.setCatalog3List(thirdLevelCats.stream().map(thirdLevelCat -> {
                        Catelog3VO catelog3VO = new Catelog3VO();
                        catelog3VO.setCatalog2Id(secondLevelCat.getCatId().toString());
                        catelog3VO.setName(thirdLevelCat.getName());
                        catelog3VO.setId(thirdLevelCat.getCatId().toString());
                        return catelog3VO;
                    }).toList());
                }
                return catelog2VO;
            }).toList();
            result.put(firstLevelCatIdString, catelog2VOs);
        }
        return result;
    }
}