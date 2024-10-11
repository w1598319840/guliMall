package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.constant.WareConstant;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.ware.dao.PurchaseDao;
import cn.wjk.gulimall.ware.dao.PurchaseDetailDao;
import cn.wjk.gulimall.ware.domain.dto.WareMergeDTO;
import cn.wjk.gulimall.ware.domain.entity.PurchaseDetailEntity;
import cn.wjk.gulimall.ware.domain.entity.PurchaseEntity;
import cn.wjk.gulimall.ware.service.PurchaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("purchaseService")
@RequiredArgsConstructor
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailDao purchaseDetailDao;

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
        List<PurchaseEntity> purchaseEntities = lambdaQuery()
                .eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatus.NEW.getStatus())
                .or()
                .eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatus.ASSIGNED.getStatus())
                .list();
        PageUtils pageUtils = PageUtils.emptyPageUtils();
        pageUtils.setList(purchaseEntities);
        return pageUtils;
    }

    @Override
    @Transactional
    public void merge(WareMergeDTO wareMergeDTO) {
        if (wareMergeDTO.getItems() == null || wareMergeDTO.getItems().isEmpty()) {
            return;
        }
        Long purchaseId = wareMergeDTO.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            //用户没有选择采购单，自动创建一个采购单
            purchaseEntity.setPriority(1);
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.NEW.getStatus());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<Long> itemIds = wareMergeDTO.getItems();
        purchaseDetailDao.update(new UpdateWrapper<PurchaseDetailEntity>()
                .set("purchase_id", purchaseId)
                .set("status", WareConstant.PurchaseDetailStatus.ASSIGNED.getStatus())
                .in("id", itemIds));

        this.lambdaUpdate()
                .set(PurchaseEntity::getUpdateTime, new Date())
                .eq(PurchaseEntity::getId, purchaseId)
                .update();
    }
}