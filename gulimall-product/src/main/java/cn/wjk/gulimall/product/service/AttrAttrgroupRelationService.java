package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.dto.AttrAttrgroupRelationDTO;
import cn.wjk.gulimall.product.domain.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除属性与分组之间的关联信息
     */
    void deleteAttrRelation(List<AttrAttrgroupRelationDTO> relationDTOs);

    /**
     * 添加关系
     */
    void addRelation(List<AttrAttrgroupRelationDTO> relationDTOs);
}

