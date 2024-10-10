package cn.wjk.gulimall.coupon.controller;

import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.coupon.entity.SkuFullReductionEntity;
import cn.wjk.gulimall.coupon.service.SkuFullReductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品满减信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:34:15
 */
@RestController
@RequestMapping("coupon/skufullreduction")
@RequiredArgsConstructor
public class SkuFullReductionController {
    private final SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:skufullreduction:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuFullReductionService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:skufullreduction:info")
    public R info(@PathVariable("id") Long id) {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);
        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:skufullreduction:save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.save(skuFullReduction);
        return R.ok();
    }

    /**
     * 保存满减信息
     */
    @PostMapping("/save/reduction")
    public R saveSkuReduction(@RequestBody List<SkuReductionTO> skuReductionTOs) {
        skuFullReductionService.saveSkuReduction(skuReductionTOs);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:skufullreduction:update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.updateById(skuFullReduction);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:skufullreduction:delete")
    public R delete(@RequestBody Long[] ids) {
        skuFullReductionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
