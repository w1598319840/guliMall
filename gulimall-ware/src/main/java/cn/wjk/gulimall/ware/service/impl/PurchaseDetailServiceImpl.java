package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.ware.dao.PurchaseDetailDao;
import cn.wjk.gulimall.ware.domain.entity.PurchaseDetailEntity;
import cn.wjk.gulimall.ware.service.PurchaseDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
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
        Page<PurchaseDetailEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("id", key)
                        .or()
                        .eq("sku_id", key)
                        .or()
                        .eq("purchase_id", key)
        );
        queryWrapper.eq(pageDTO.getStatus() != null, "status", pageDTO.getStatus());
        Long wareId = pageDTO.getWareId();
        queryWrapper.eq(wareId != null && !wareId.equals(0L), "ware_id", wareId);
        this.page(page, queryWrapper);

        return new PageUtils(page);
    }
}