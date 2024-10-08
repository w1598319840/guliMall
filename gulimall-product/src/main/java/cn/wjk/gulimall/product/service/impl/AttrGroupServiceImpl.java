package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrGroupDao;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.domain.vo.AttrGroupVO;
import cn.wjk.gulimall.product.service.AttrGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
@RequiredArgsConstructor
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final CategoryServiceImpl categoryService;

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
}