package cn.wjk.gulimall.coupon.controller;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.coupon.entity.SpuBoundsEntity;
import cn.wjk.gulimall.coupon.service.SpuBoundsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品spu积分设置
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:34:15
 */
@RestController
@RequestMapping("coupon/spubounds")
@RequiredArgsConstructor
public class SpuBoundsController {
    private final SpuBoundsService spuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:spubounds:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuBoundsService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:spubounds:info")
    public R info(@PathVariable("id") Long id) {
        SpuBoundsEntity spuBounds = spuBoundsService.getById(id);
        return R.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     * 其实实际上远程调用就是 实体 -> json -> 实体 的过程，因此只需要里面的字段名保持一致，就算对象不一样，也能够成功转换
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuBoundsEntity spuBounds) {
        spuBoundsService.save(spuBounds);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuBoundsEntity spuBounds) {
        spuBoundsService.updateById(spuBounds);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:spubounds:delete")
    public R delete(@RequestBody Long[] ids) {
        spuBoundsService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
