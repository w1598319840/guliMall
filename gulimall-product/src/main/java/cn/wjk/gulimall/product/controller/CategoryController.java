package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.product.domain.vo.CategoryVO;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import cn.wjk.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("product/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 查询所有分类以及子分类，以树形结构返回
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryVO> entities = categoryService.listWithTree();

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryVO categoryVO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(categoryVO, categoryEntity);
        categoryService.save(categoryEntity);

        return R.ok();
    }

    /**
     * 拖拽分类以改变顺序
     */
    @PostMapping("/update/sort")
    public R update(@RequestBody List<CategoryEntity> category) {
        categoryService.updateBatchById(category);
        return R.ok();
    }

    /**
     * 级联修改(由于逻辑外键的存在)
     *
     */
    @PostMapping("/update")
    public R updateCascade(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        if (catIds.length == 0) {
            return R.ok();
        }
        categoryService.removeCategoryByIds(Arrays.asList(catIds));
        return R.ok();
    }
}
