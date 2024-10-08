package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.dto.AttrDTO;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.vo.AttrVO;
import com.baomidou.mybatisplus.extension.service.IService;

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
     */
    PageUtils getCatelogAttr(Long catelogId, PageDTO pageDTO);

    /**
     * 获取详细信息
     */
    AttrVO getDetail(Long attrId);

    /**
     * 级联修改
     */
    void updateCascade(AttrDTO attrDTO);
}

