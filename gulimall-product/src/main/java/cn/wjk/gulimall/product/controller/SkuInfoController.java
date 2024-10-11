package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.product.service.SkuInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * sku信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("product/skuinfo")
@RequiredArgsConstructor
public class SkuInfoController {
    private final SkuInfoService skuInfoService;

    /**
     * 分页查询sku
     */
    @RequestMapping("/list")
    public R list(PageDTO pageDTO) {
        PageUtils page = skuInfoService.pageQuerySku(pageDTO);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

    @GetMapping("/info/skuName")
    public R getSkuNamesBySkuIds(@RequestParam List<Long> skuIds) {
        Map<Long, String> map = skuInfoService.getSkuNamesBySkuIds(skuIds);
        return R.ok().put("data", map);
    }
}
