package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.dto.CategoryBrandRelationDTO;
import cn.wjk.gulimall.product.domain.entity.CategoryBrandRelationEntity;
import cn.wjk.gulimall.product.domain.vo.BrandVO;
import cn.wjk.gulimall.product.service.BrandService;
import cn.wjk.gulimall.product.service.CategoryBrandRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 品牌分类关联
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("product/categorybrandrelation")
@RequiredArgsConstructor
public class CategoryBrandRelationController {
    private final CategoryBrandRelationService categoryBrandRelationService;
    private final BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.getRelatedCategoryByBrandId(brandId);
        return R.ok().put("data", list);
    }

    /**
     * 获取分类关联的品牌
     */
    @GetMapping("/brands/list")
    public R brandsList(@RequestParam("catId") Long catId) {
        List<BrandVO> brandVOs = brandService.brandsList(catId);
        return R.ok().put("data", brandVOs);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationDTO categoryBrandRelationDTO) {
        categoryBrandRelationService.saveDetail(categoryBrandRelationDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
