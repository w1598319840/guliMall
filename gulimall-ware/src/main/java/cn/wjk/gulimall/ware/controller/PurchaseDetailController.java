package cn.wjk.gulimall.ware.controller;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.ware.entity.PurchaseDetailEntity;
import cn.wjk.gulimall.ware.service.PurchaseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 *
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
@RestController
@RequestMapping("ware/purchasedetail")
@RequiredArgsConstructor
public class PurchaseDetailController {
    private final PurchaseDetailService purchaseDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("ware:purchasedetail:list")
    public R list(PageDTO pageDTO) {
        PageUtils page = purchaseDetailService.pageQueryDetail(pageDTO);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchasedetail:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchasedetail:save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchasedetail:update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail) {
        purchaseDetailService.updateById(purchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchasedetail:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
