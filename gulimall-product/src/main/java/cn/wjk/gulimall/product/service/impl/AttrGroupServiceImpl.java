package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrGroupDao;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.service.AttrGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

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
        lambdaQuery()
                .eq(!catelogId.equals(0L), AttrGroupEntity::getCatelogId, catelogId)
                .and(key != null, (wrapper) ->
                        wrapper.like(AttrGroupEntity::getAttrGroupName, key)
                                .or()
                                .like(AttrGroupEntity::getAttrGroupId, key)
                                .or()
                                .like(AttrGroupEntity::getDescript, key))
                .orderBy(pageDTO.getSidx() != null, isAsc, AttrGroupEntity::getAttrGroupName)
                .page(page);

        return new PageUtils(page);
    }

}