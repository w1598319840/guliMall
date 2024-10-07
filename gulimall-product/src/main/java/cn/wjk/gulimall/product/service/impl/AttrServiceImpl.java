package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.AttrAttrgroupRelationDao;
import cn.wjk.gulimall.product.dao.AttrDao;
import cn.wjk.gulimall.product.domain.dto.AttrDTO;
import cn.wjk.gulimall.product.domain.entity.AttrAttrgroupRelationEntity;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.service.AttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service("attrService")
@RequiredArgsConstructor
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;

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
}