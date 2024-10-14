package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.constant.RedisConstants;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.CategoryBrandRelationDao;
import cn.wjk.gulimall.product.dao.CategoryDao;
import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import cn.wjk.gulimall.product.domain.vo.CategoryVO;
import cn.wjk.gulimall.product.domain.vo.Catelog2VO;
import cn.wjk.gulimall.product.domain.vo.Catelog3VO;
import cn.wjk.gulimall.product.service.CategoryService;
import cn.wjk.gulimall.product.utils.RedisUtils;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryBrandRelationDao categoryBrandRelationDao;
    private final RedisUtils redisUtils;
    private final StringRedisTemplate stringRedisTemplate;

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

    /**
     * 直接上redis
     * 我们需要解决缓存的三大问题:
     *  缓存穿透: 缓存空值null
     *  缓存雪崩: 为expire添加offset
     *  缓存击穿: 缓存失效重新缓存时添加锁(分布式锁虽好，但性能较低)
     */
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() {
        Map<String, List<Catelog2VO>> result;
        long start = System.currentTimeMillis();
        result = redisUtils.getCacheObject(RedisConstants.PRODUCT_CATALOG_JSON_DATA_KEY, new TypeReference<>() {
        }, RedisConstants.PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME);
        long end = System.currentTimeMillis();
        log.info("redis:{} ms", end - start);
        if (result != null) {
            //缓存存在
            return result;
        }

//        result = getCatalogJsonFromDBWithLocalLock();
        result = getCatalogJsonFromDBWithRedisLock();
        return result;
    }

    /**
     * 也是一种解决方案
     */
    @Deprecated
    public Map<String, List<Catelog2VO>> getCatalogJsonDeprecated2() {
        List<CategoryEntity> allCategories = this.lambdaQuery().list();
        List<CategoryEntity> firstLevel = getCategoryByParentId(allCategories, 0);
        return firstLevel.stream().collect(Collectors.toMap(
                categoryEntity -> categoryEntity.getCatId().toString(),
                categoryEntity -> {
                    List<CategoryEntity> secondLevel = getCategoryByParentId(allCategories, categoryEntity.getCatId());
                    List<Catelog2VO> catelog2VOs = Collections.emptyList();
                    if (!secondLevel.isEmpty()) {
                        catelog2VOs = secondLevel.stream().map(l2 -> {
                            Long catId = l2.getCatId();
                            Catelog2VO catelog2VO = new Catelog2VO();
                            catelog2VO.setCatalog1Id(categoryEntity.getCatId().toString());
                            catelog2VO.setName(l2.getName());
                            catelog2VO.setId(catId.toString());
                            List<Catelog3VO> thirdLevel
                                    = getCategoryByParentId(allCategories, catId).stream().map(l3 -> {
                                Catelog3VO catelog3VO = new Catelog3VO();
                                catelog3VO.setId(l3.getCatId().toString());
                                catelog3VO.setName(l3.getName());
                                catelog3VO.setCatalog2Id(catId.toString());
                                return catelog3VO;
                            }).toList();
                            catelog2VO.setCatalog3List(thirdLevel);
                            return catelog2VO;
                        }).toList();
                    }
                    return catelog2VOs;
                }
        ));
    }

    private List<CategoryEntity> getCategoryByParentId(List<CategoryEntity> allCategories, long parentId) {
        return allCategories.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(parentId)).toList();
    }

    /**
     * 基于redis分布式锁的查询数据库
     */
    public synchronized Map<String, List<Catelog2VO>> getCatalogJsonFromDBWithRedisLock() {
        ValueOperations<String, String> stringOps = stringRedisTemplate.opsForValue();
        boolean flag;
        //为防止后续解锁时由于业务耗时太长，锁过期而到期删了别人的锁，我们需要设上当前线程的唯一标识
        //暂时使用UUID，后续感觉还是使用userId比较好
        String uuid = UUID.randomUUID().toString();
        Map<String, List<Catelog2VO>> result;
        try {
            flag = Boolean.TRUE.equals(stringOps.setIfAbsent(RedisConstants.PRODUCT_CATALOG_JSON_LOCK_KEY, uuid,
                    RedisConstants.PRODUCT_CATALOG_JSON_LOCK_EXPIRE_TIME));
            while (!flag) {
                //自旋重试
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                flag = Boolean.TRUE.equals(stringOps.setIfAbsent(RedisConstants.PRODUCT_CATALOG_JSON_LOCK_KEY, uuid,
                        RedisConstants.PRODUCT_CATALOG_JSON_LOCK_EXPIRE_TIME));
            }
            //成功获取到锁了
            log.info("获取分布式锁成功");
            //double check一下
            result = redisUtils.getCacheObject(RedisConstants.PRODUCT_CATALOG_JSON_DATA_KEY,
                    new TypeReference<>() {
                    }, RedisConstants.PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME);
            if (result != null) {
                //此时其他线程已经添加过缓存了
                return result;
            }
            result = getCatalogJsonFromDB();
            //加缓存
            redisUtils.setCache(RedisConstants.PRODUCT_CATALOG_JSON_DATA_KEY,
                    result, RedisConstants.PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME);
        } finally {
            //解锁
            //在finally中保证一定能够解锁
            //解锁时要判断是不是自己的锁，并且判断和解锁必须是原子操作，那么我们就需要使用lua脚本
//        String value = stringOps.get(RedisConstants.PRODUCT_CATALOG_JSON_LOCK_KEY);
//        if (uuid.equals(value)) {
//            stringRedisTemplate.delete(RedisConstants.PRODUCT_CATALOG_JSON_LOCK_KEY);
//        }
            //判断并解锁成功返回1，失败返回0(其实返回值不重要，只要判断并解锁的操作是原子性的就好了)
            String scriptString = """
                    if (redis.call('get', KEYS[1]) == ARGV[1])
                    then
                        return redis.call('del',KEYS[1])
                    end
                    return 0;
                    """;
            //构造器传入脚本、返回类型(也可以通过泛型指定返回类型)
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(scriptString, Long.class);
            //判断并解锁，返回值不重要
            stringRedisTemplate.execute(script, List.of(RedisConstants.PRODUCT_CATALOG_JSON_LOCK_KEY), uuid);
        }
        return result;
    }

    /**
     * 基于本地锁的查询数据库
     */
    public synchronized Map<String, List<Catelog2VO>> getCatalogJsonFromDBWithLocalLock() {
        Map<String, List<Catelog2VO>> result =
                redisUtils.getCacheObject(RedisConstants.PRODUCT_CATALOG_JSON_DATA_KEY, new TypeReference<>() {
                }, RedisConstants.PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME);
        if (result != null) {
            //此时其他线程已经添加过缓存了
            return result;
        }

        result = getCatalogJsonFromDB();
        redisUtils.setCache(RedisConstants.PRODUCT_CATALOG_JSON_DATA_KEY,
                result, RedisConstants.PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME);
        return result;
    }

    /**
     * 从数据库中查询
     */
    private Map<String, List<Catelog2VO>> getCatalogJsonFromDB() {
        Map<String, List<Catelog2VO>> result;
        long start = System.currentTimeMillis();
        List<CategoryEntity> allCategories = this.lambdaQuery().list();
        long end = System.currentTimeMillis();
        log.info("database:{} ms", end - start);
        Map<Integer, List<CategoryEntity>> catLevelToCategoryMap =
                allCategories.stream().collect(Collectors.groupingBy(CategoryEntity::getCatLevel));
        result = new HashMap<>();
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
    public Map<String, List<Catelog2VO>> getCatalogJsonDeprecated1() {
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