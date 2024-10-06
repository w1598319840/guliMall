package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.domain.dto.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils listByCatelogId(Long catelogId, PageDTO pageDTO);
}

