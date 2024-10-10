package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.dto.spuSaveDto.SpuSaveDTO;
import cn.wjk.gulimall.product.domain.entity.SpuInfoEntity;
import cn.wjk.gulimall.product.service.SpuInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * spu信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("product/spuinfo")
@RequiredArgsConstructor
public class SpuInfoController {
    private final SpuInfoService spuInfoService;

    /**
     * 复杂的分页查询
     */
    @RequestMapping("/list")
    public R list(PageDTO pageDTO) {
        PageUtils page = spuInfoService.queryPageByCondition(pageDTO);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);
        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存Spu的信息，该信息十分复杂
     */
    @PostMapping("/save")
    public R saveDetail(@RequestBody SpuSaveDTO spuSaveDTO) {
        spuInfoService.saveSpuInfo(spuSaveDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
