package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.constant.ProductConstant;
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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        if (attrDTO.getAttrType().equals(ProductConstant.AttrType.ATTR_TYPE_SALE.getCode())) {
            //如果时销售属性，直接返回，不需要保存分组信息
            return;
        }
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrDTO.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils getCatelogAttr(Long catelogId, PageDTO pageDTO, String attrType) {
        if (catelogId == null
                || !(attrType.equalsIgnoreCase(ProductConstant.AttrType.ATTR_TYPE_BASE.getType())
                || attrType.equalsIgnoreCase(ProductConstant.AttrType.ATTR_TYPE_SALE.getType()))) {
            return null;
        }
        int type = attrType.equalsIgnoreCase(ProductConstant.AttrType.ATTR_TYPE_BASE.getType())
                ? ProductConstant.AttrType.ATTR_TYPE_BASE.getCode()
                : ProductConstant.AttrType.ATTR_TYPE_SALE.getCode();
        boolean isAsc = "asc".equals(pageDTO.getOrder());
        String key = pageDTO.getKey();
        Page<AttrEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!catelogId.equals(0L), "catelog_id", catelogId)
                .eq("attr_type", type)
                .orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx())
                .and(StringUtils.isNotEmpty(key), wrapper ->
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
        if (attrEntity.getAttrType() == ProductConstant.AttrType.ATTR_TYPE_SALE.getCode()) {
            //如果是销售属性，不需要查询分组信息
            return attrVO;
        }
        attrVO.setAttrGroupId(attrAttrgroupRelationDao.selectOne(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId()
        );
        return attrVO;
    }

    @Override
    @Transactional
    public void updateCascade(AttrDTO attrDTO) {
        Long attrId = attrDTO.getAttrId();
        Long attrGroupId = attrDTO.getAttrGroupId();
        if (attrId == null) {
            return;
        }
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDTO, attrEntity);
        updateById(attrEntity);
        //级联修改
        //pms_attr_attrgroup_relation表
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId)
        );
        if (relationEntity == null && attrDTO.getAttrType() == ProductConstant.AttrType.ATTR_TYPE_BASE.getCode()) {
            //关联表中不存在关联数据并且是规格参数时，才需要向关联表中插入数据
            relationEntity = new AttrAttrgroupRelationEntity(null, attrId, attrGroupId, null);
            attrAttrgroupRelationDao.insert(relationEntity);
            return;
        }
        if (relationEntity != null && attrDTO.getAttrType() == ProductConstant.AttrType.ATTR_TYPE_BASE.getCode()) {
            //关联表中存在数据并且是规格参数时，才需要修改关联表中的数据
            attrAttrgroupRelationDao.update(new UpdateWrapper<AttrAttrgroupRelationEntity>()
                    .set("attr_group_id", attrGroupId)
                    .eq("attr_id", attrId)
            );
        }
    }

    @Override
    public List<AttrEntity> getAllAttrRelatedToAttrGroup(Long attrGroupId) {
        if (attrGroupId == null) {
            return Collections.emptyList();
        }

        List<Long> attrIdList = attrAttrgroupRelationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId)
        ).stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();

        if (attrIdList.isEmpty()) {
            return Collections.emptyList();
        }

        return listByIds(attrIdList);
    }

    @Override
    @Transactional
    public PageUtils getUnrelatedAttrInTheSameCatelog(Long attrGroupId, PageDTO pageDTO) {
        if (attrGroupId == null) {
            return null;
        }

        final String ATTR_GROUP_ID = "attr_group_id";
        final String CATELOG_ID = "catelog_id";
        //1. 先查询出本分类
        Long catelogId = attrGroupDao.selectById(attrGroupId).getCatelogId();

        //2. 再查询出本分类下所有的属性的id
        List<Long> allAttrIdList = list(new QueryWrapper<AttrEntity>().eq(CATELOG_ID, catelogId))
                .stream().map(AttrEntity::getAttrId).toList();

        //3. 再查询出本分类下所有已经被本分类下的属性分组引用过的属性的id
        //3.1. 查询出本分类下所有的属性分组的id
        List<Long> allAttrGroupIdList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq(CATELOG_ID, catelogId)
        ).stream().map(AttrGroupEntity::getAttrGroupId).toList();
        //3.2. 查询出本分类下的所有属性分组引用过的属性id
        List<Long> relatedAttrIdList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .in(ATTR_GROUP_ID, allAttrGroupIdList))
                .stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();

        //4. 然后把分页参数整好
        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        Page<AttrEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("attr_id", key)
                        .or()
                        .like("attr_name", key)
                        .or()
                        .like("value_select", key)
        );

        //5. 最后整点其他的查询参数
        //5.1. 所有要查的属性的id(本分类下所有属性id-本分类下已经被其他属性分组关联过的属性id)
        List<Long> attrIds = allAttrIdList.stream().filter(attrId -> !relatedAttrIdList.contains(attrId)).toList();
        if (attrIds.isEmpty()) {
            //当前分类下已经没有没有被关联过的属性了
            return null;
        }
        queryWrapper.in("attr_id", attrIds);
        //5.2. 要是基本属性
        queryWrapper.eq("attr_type", ProductConstant.AttrType.ATTR_TYPE_BASE.getCode());

        //6. 总算搞完了，广快查询
        page(page, queryWrapper);

        //7. 返回结果
        return new PageUtils(page);
    }
}