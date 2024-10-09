package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrAttrgroupRelationDao;
import cn.wjk.gulimall.product.dao.AttrDao;
import cn.wjk.gulimall.product.dao.AttrGroupDao;
import cn.wjk.gulimall.product.domain.entity.AttrAttrgroupRelationEntity;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.domain.vo.AttrGroupVO;
import cn.wjk.gulimall.product.service.AttrGroupService;
import cn.wjk.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("attrGroupService")
@RequiredArgsConstructor
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final CategoryService categoryService;
    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    private final AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils listByCatelogId(Long catelogId, PageDTO pageDTO) {
        String order = pageDTO.getOrder();
        boolean isAsc = "asc".equalsIgnoreCase(order);
        String key = pageDTO.getKey();

        /*
        select
            *
        from
            pms_attr_group where attr_group_id = catelogId
        and
            (attr_group_id like xxx or descript like xx or attr_group_name like xxx)
        order by
            xxx xxx
         */
        Page<AttrGroupEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!catelogId.equals(0L), "catelog_id", catelogId);
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("attr_group_id", key)
                        .or()
                        .like("attr_group_name", key)
                        .or()
                        .like("descript", key));
        page(page, queryWrapper);
        return new PageUtils(page);
    }

    @Override
    public AttrGroupVO getInfo(Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = lambdaQuery().eq(AttrGroupEntity::getAttrGroupId, attrGroupId).one();
        AttrGroupVO attrGroupVO = new AttrGroupVO();
        BeanUtils.copyProperties(attrGroupEntity, attrGroupVO);
        attrGroupVO.setCatelogPath(categoryService.getCatelogPathById(attrGroupEntity.getCatelogId()));
        return attrGroupVO;
    }

    @Override
    public List<AttrGroupVO> getAttrGroupWithAttr(Long catelogId) {
        if (catelogId == null) {
            return Collections.emptyList();
        }

        //1. 查询本分类下的所有分组
        List<AttrGroupVO> attrGroupVOList = lambdaQuery().eq(AttrGroupEntity::getCatelogId, catelogId).list()
                .stream().map(attrGroupEntity -> {
                    AttrGroupVO attrGroupVO = new AttrGroupVO();
                    BeanUtils.copyProperties(attrGroupEntity, attrGroupVO);
                    return attrGroupVO;
                }).toList();

        //2. 查询本分类下所有分组的属性
        //2.1. 将所有的要查询的分组id收集起来
        List<Long> attrGroupIdList = attrGroupVOList.stream().map(AttrGroupVO::getAttrGroupId).toList();
        if (attrGroupIdList.isEmpty()) {
            return Collections.emptyList();
        }
        //2.2. 查询出 分组id 与 该分组下的所有属性的id 的对应关系
        Map<Long, List<Long>> attrGroupIdToAttrIdMap
                = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .in("attr_group_id", attrGroupIdList))
                .stream()
                .collect(Collectors.groupingBy(AttrAttrgroupRelationEntity::getAttrGroupId,
                        //下面这部分可以看作是对Map中的value再进行一次stream操作
                        //第一个参数为`intermediate operation`，第二个参数为`terminal operation`
                        Collectors.mapping(AttrAttrgroupRelationEntity::getAttrId, Collectors.toList())));

        //2.3. 查询出所有分组下所有的属性
        List<Long> allAttrIds = attrGroupIdToAttrIdMap.values().stream().flatMap(List::stream).toList();
        List<AttrEntity> allAttr = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", allAttrIds));

        //2.4. 分组，将groupId和attrEntity对应起来
        HashMap<Long, List<AttrEntity>> attrGroupIdToAttrEntityMap = new HashMap<>();
        for (Map.Entry<Long, List<Long>> attrGroupIdToAttrIdsEntry : attrGroupIdToAttrIdMap.entrySet()) {
            List<Long> AttrIds = attrGroupIdToAttrIdsEntry.getValue();
            ArrayList<AttrEntity> attrEntities = new ArrayList<>(allAttr.stream()
                    .filter(attrEntity -> AttrIds.contains(attrEntity.getAttrId()))
                    .toList());
            attrGroupIdToAttrEntityMap.put(attrGroupIdToAttrIdsEntry.getKey(), attrEntities);
        }

        //2.5. 把这些attrEntity赋值进去
        attrGroupVOList.forEach(attrGroupVO ->
                attrGroupVO.setAttrs(attrGroupIdToAttrEntityMap.get(attrGroupVO.getAttrGroupId())));

        return attrGroupVOList;
    }
}