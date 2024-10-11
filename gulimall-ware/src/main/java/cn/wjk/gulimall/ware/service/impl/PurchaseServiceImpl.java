package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.ware.dao.PurchaseDao;
import cn.wjk.gulimall.ware.entity.PurchaseEntity;
import cn.wjk.gulimall.ware.service.PurchaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getUnreceive() {
        List<PurchaseEntity> purchaseEntities = lambdaQuery().eq(PurchaseEntity::getStatus, 0)
                .or()
                .eq(PurchaseEntity::getStatus, 1)
                .list();
        PageUtils pageUtils = PageUtils.emptyPageUtils();
        pageUtils.setList(purchaseEntities);
        return pageUtils;
    }
}