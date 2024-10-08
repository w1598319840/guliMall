package cn.wjk.gulimall.product.controller;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.product.domain.dto.AttrAttrgroupRelationDTO;
import cn.wjk.gulimall.product.domain.entity.AttrEntity;
import cn.wjk.gulimall.product.domain.entity.AttrGroupEntity;
import cn.wjk.gulimall.product.domain.vo.AttrGroupVO;
import cn.wjk.gulimall.product.service.AttrAttrgroupRelationService;
import cn.wjk.gulimall.product.service.AttrGroupService;
import cn.wjk.gulimall.product.service.AttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@RestController
@RequestMapping("product/attrgroup")
@RequiredArgsConstructor
public class AttrGroupController {
    private final AttrGroupService attrGroupService;
    private final AttrService attrService;
    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取分类属性分组
     *
     * @param catelogId 分类id
     * @param pageDTO 分页查询dto
     * @return 返回分压查询数据
     */
    @GetMapping("/list/{catelogId}")
    public R listByCatelogId(@PathVariable("catelogId") Long catelogId, PageDTO pageDTO) {
        PageUtils pageUtils = attrGroupService.listByCatelogId(catelogId, pageDTO);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupVO attrGroupVO = attrGroupService.getInfo(attrGroupId);
        return R.ok().put("attrGroup", attrGroupVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 获取指定属性分组的所有属性
     *
     * @param attrGroupId 属性分组的id
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R getAllAttrRelatedToAttrGroup(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrEntities = attrService.getAllAttrRelatedToAttrGroup(attrGroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 删除属性与分组之间的关联信息
     */
    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody List<AttrAttrgroupRelationDTO> relationDTOS) {
        attrAttrgroupRelationService.deleteAttrRelation(relationDTOS);
        return R.ok();
    }

    /**
     * 获取属性分组里面还没有关联的本分类里面的其他基本属性，方便添加新的关联
     *
     * @param attrGroupId 当前属性分组
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R getUnrelatedAttrInTheSameCatelog(@PathVariable("attrGroupId") Long attrGroupId, PageDTO pageDTO) {
        PageUtils pageUtils = attrService.getUnrelatedAttrInTheSameCatelog(attrGroupId, pageDTO);
        return R.ok().put("page", pageUtils);
    }
}
