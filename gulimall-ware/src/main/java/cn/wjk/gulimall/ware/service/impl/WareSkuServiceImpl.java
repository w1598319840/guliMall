package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.ware.dao.WareSkuDao;
import cn.wjk.gulimall.ware.entity.WareSkuEntity;
import cn.wjk.gulimall.ware.service.WareSkuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils pageQueryDetail(PageDTO pageDTO) {
        if (pageDTO == null) {
            return PageUtils.emptyPageUtils();
        }

        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        Page<WareSkuEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        Long wareId = pageDTO.getWareId();
        Long skuId = pageDTO.getSkuId();
        queryWrapper.eq(wareId != null && !wareId.equals(0L), "ware_id", wareId);
        queryWrapper.eq(skuId != null && !skuId.equals(0L), "sku_id", skuId);
        this.page(page, queryWrapper);

        return new PageUtils(page);
    }
}