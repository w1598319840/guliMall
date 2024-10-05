package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.entity.vo.CategoryVO;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.entity.CategoryEntity;
import cn.wjk.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
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

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeCategoryByIds(Arrays.asList(catIds));
        return R.ok();
    }
}
