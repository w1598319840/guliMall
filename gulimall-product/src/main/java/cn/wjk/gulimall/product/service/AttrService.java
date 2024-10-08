package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.dto.AttrDTO;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.vo.AttrVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存规格参数以及其属性分组的信息
     */
    void saveAttrAndGroupInfo(AttrDTO attrDTO);

    /**
     * 获取分类的规格参数
     * attrType: base(规格参数)/sale(销售属性)
     */
    PageUtils getCatelogAttr(Long catelogId, PageDTO pageDTO, String attrType);

    /**
     * 获取详细信息
     */
    AttrVO getDetail(Long attrId);

    /**
     * 级联修改
     */
    void updateCascade(AttrDTO attrDTO);

    /**
     * 获取指定属性分组的所有属性
     */
    List<AttrEntity> getAllAttrRelatedToAttrGroup(Long attrGroupId);

    /**
     * 获取属性分组里面还没有关联的本分类里面的其他基本属性，方便添加新的关联
     *
     * @param attrGroupId 当前的属性分组
     * @param pageDTO 分页参数
     * @return 返回本分类中当前属性分组还未关联的基本属性
     */
    PageUtils getUnrelatedAttrInTheSameCatelog(Long attrGroupId, PageDTO pageDTO);
}

