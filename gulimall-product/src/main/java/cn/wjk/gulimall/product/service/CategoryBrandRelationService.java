package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.entity.CategoryBrandRelationEntity;
import cn.wjk.gulimall.product.domain.vo.CategoryBrandRelationVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取品牌关联的分类
     */
    List<CategoryBrandRelationVO> getRelatedCategoryByBrandId(Long brandId);
}

