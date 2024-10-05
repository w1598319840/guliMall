package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.entity.vo.CategoryVO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 以森林形式的结果显示查询结果
     * @return 返回森林中各个树的的根节点
     */
    List<CategoryVO> listWithTree();

    /**
     * 根据id删除分类
     * @param ids 分类id列表
     */
    void removeCategoryByIds(List<Long> ids);
}

