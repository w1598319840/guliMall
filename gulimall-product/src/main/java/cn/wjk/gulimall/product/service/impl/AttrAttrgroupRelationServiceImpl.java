package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrAttrgroupRelationDao;
import cn.wjk.gulimall.product.domain.dto.AttrAttrgroupRelationDTO;
import cn.wjk.gulimall.product.domain.entity.AttrAttrgroupRelationEntity;
import cn.wjk.gulimall.product.service.AttrAttrgroupRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteAttrRelation(List<AttrAttrgroupRelationDTO> relationDTOs) {
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        for (AttrAttrgroupRelationDTO relationDTO : relationDTOs) {
            queryWrapper.or(wrapper -> wrapper.eq("attr_id", relationDTO.getAttrId())
                    .eq("attr_group_id", relationDTO.getAttrGroupId()));
        }
        remove(queryWrapper);
    }
}