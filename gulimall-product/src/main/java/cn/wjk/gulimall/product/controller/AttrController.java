package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.domain.to.AttrTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.dto.AttrDTO;
import cn.wjk.gulimall.product.domain.dto.UpdateSpuAttrDTO;
import cn.wjk.gulimall.product.domain.entity.ProductAttrValueEntity;
import cn.wjk.gulimall.product.domain.vo.AttrVO;
import cn.wjk.gulimall.product.service.AttrService;
import cn.wjk.gulimall.product.service.ProductAttrValueService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品属性
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("/product/attr")
@RequiredArgsConstructor
public class AttrController {
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 获取分类的规格参数
     *
     * @param catelogId 分类id，若为0则查所有
     * @param pageDTO 分页查询参数
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R getCatelogAttr(@PathVariable("attrType") String attrType,
                            @PathVariable("catelogId") Long catelogId,
                            PageDTO pageDTO) {
        PageUtils pageUtils = attrService.getCatelogAttr(catelogId, pageDTO, attrType);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrVO attrVO = attrService.getDetail(attrId);

        return R.ok().put("attr", attrVO);
    }

    /**
     * 保存规格参数以及其所属属性分组的信息
     */
    @RequestMapping("/save")
    public R saveAttrAndGroupInfo(@RequestBody AttrDTO attrDTO) {
        attrService.saveAttrAndGroupInfo(attrDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody AttrDTO attrDTO) {
        attrService.updateCascade(attrDTO);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeCascadeByIds(Arrays.asList(attrIds));
        return R.ok();
    }

    /**
     * 获取spu的规格信息
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R listForSpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> data = productAttrValueService.listForSpu(spuId);
        return R.ok().put("data", data);
    }

    /**
     * 修改商品规格
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId, @RequestBody List<UpdateSpuAttrDTO> updateSpuAttrs) {
        productAttrValueService.updateSpuAttr(spuId, updateSpuAttrs);
        return R.ok();
    }

    /**
     * 根据attrId获取attrName
     */
    @GetMapping("/name")
    public R getAttrNameByAttrIds(@RequestParam("attrIds") List<Long> attrIds) {
        List<AttrTO> attrTOs = attrService.getAttrNameByIds(attrIds);
        return R.ok().put("data", JSON.toJSONString(attrTOs));
    }
}