package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrAttrgroupRelationDao;
import cn.wjk.gulimall.product.dao.AttrDao;
import cn.wjk.gulimall.product.dao.AttrGroupDao;
import cn.wjk.gulimall.product.dao.CategoryDao;
import cn.wjk.gulimall.product.domain.dto.AttrDTO;
import cn.wjk.gulimall.product.domain.entity.AttrAttrgroupRelationEntity;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import cn.wjk.gulimall.product.domain.vo.AttrVO;
import cn.wjk.gulimall.product.service.AttrService;
import cn.wjk.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("attrService")
@RequiredArgsConstructor
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    private final CategoryDao categoryDao;
    private final AttrGroupDao attrGroupDao;
    private final CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttrAndGroupInfo(AttrDTO attrDTO) {
        //保存本身信息
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDTO, attrEntity);
        this.save(attrEntity);
        //保存分组信息
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrDTO.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils getCatelogAttr(Long catelogId, PageDTO pageDTO) {
        if (catelogId == null) {
            return null;
        }
        boolean isAsc = "asc".equals(pageDTO.getOrder());
        String key = pageDTO.getKey();
        Page<AttrEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!catelogId.equals(0L), "catelog_id", catelogId);
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("attr_id", key)
                        .or()
                        .like("attr_name", key)
                        .or()
                        .like("value_select", key)
        );
        page(page, queryWrapper);
        List<AttrEntity> entities = page.getRecords();

        //一次性将所有的catelogName以及attrGroupName都查询出来
        Set<Long> catelogIdList = entities.stream().map(AttrEntity::getCatelogId).collect(Collectors.toSet());
        Map<Long, String> catelogIdToNameMap;
        if (!catelogIdList.isEmpty()) {
            catelogIdToNameMap = categoryDao.selectBatchIds(catelogIdList).stream()
                    .collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));
        } else {
            catelogIdToNameMap = new HashMap<>();
        }

        //这里我使用了两个map，一个map是attrId与attrGroupId的映射关系，另一个map是attrGroupId与attrGroupName的映射关系
        Set<Long> attrIds = entities.stream().map(AttrEntity::getAttrId).collect(Collectors.toSet());
        Map<Long, Long> attrIdToAttrGroupIdMap;
        if (!attrIds.isEmpty()) {
            attrIdToAttrGroupIdMap = attrAttrgroupRelationDao.selectList(
                            new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", attrIds)
                    ).stream()
                    .collect(Collectors.toMap(AttrAttrgroupRelationEntity::getAttrId, AttrAttrgroupRelationEntity::getAttrGroupId));
        } else {
            attrIdToAttrGroupIdMap = new HashMap<>();
        }

        Map<Long, String> attrGroupIdToAttrGroupNameMap;
        if (!attrIdToAttrGroupIdMap.values().isEmpty()) {
            attrGroupIdToAttrGroupNameMap =
                    attrGroupDao.selectBatchIds(attrIdToAttrGroupIdMap.values().stream().distinct().toList())
                            .stream()
                            .collect(Collectors.toMap(AttrGroupEntity::getAttrGroupId, AttrGroupEntity::getAttrGroupName));
        } else {
            attrGroupIdToAttrGroupNameMap = new HashMap<>();
        }

        List<AttrVO> attrVOs = entities.stream().map(attrEntity -> {
            AttrVO attrVO = new AttrVO();
            BeanUtils.copyProperties(attrEntity, attrVO);
            attrVO.setCatelogName(catelogIdToNameMap.get(attrEntity.getCatelogId()));
            attrVO.setGroupName(attrGroupIdToAttrGroupNameMap.get(attrIdToAttrGroupIdMap.get(attrEntity.getAttrId())));
            return attrVO;
        }).toList();

        return new PageUtils(page.getTotal(), page.getSize(), page.getPages(), page.getCurrent(), attrVOs);
    }

    @Override
    public AttrVO getDetail(Long attrId) {
        AttrEntity attrEntity = getById(attrId);
        AttrVO attrVO = new AttrVO();
        BeanUtils.copyProperties(attrEntity, attrVO);
        attrVO.setCatelogPath(categoryService.getCatelogPathById(attrEntity.getCatelogId()));
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao
                .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (relationEntity != null) {
            attrVO.setAttrGroupId(relationEntity.getAttrGroupId());
        }
        return attrVO;
    }
}