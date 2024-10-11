package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.constant.WareConstant;
import cn.wjk.gulimall.common.exception.PurchaseException;
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
        List<Long> items = wareMergeDTO.getItems();
        if (items == null || items.isEmpty()) {
            return;
        }
        //需要检查前端发送的purchase_detail_id对应的PurchaseDetail的状态是NEW/ASSIGN
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailDao.selectBatchIds(items)
                .stream().filter(purchaseDetail -> {
                    int status = purchaseDetail.getStatus();
                    return status != WareConstant.PurchaseStatus.NEW.getStatus()
                            && status != WareConstant.PurchaseStatus.ASSIGNED.getStatus();
                }).toList();
        if (!purchaseDetailEntities.isEmpty()) {
            //其实应该抛出异常
            throw new PurchaseException("仅能合并新建、已分配状态的采购需求");
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
        purchaseDetailDao.update(new UpdateWrapper<PurchaseDetailEntity>()
                .set("purchase_id", purchaseId)
                .set("status", WareConstant.PurchaseDetailStatus.ASSIGNED.getStatus())
                .in("id", items));

        this.lambdaUpdate()
                .set(PurchaseEntity::getUpdateTime, new Date())
                .eq(PurchaseEntity::getId, purchaseId)
                .update();
    }

    @Override
    @Transactional
    public void receivePurchase(List<Long> ids) {
        //0. 判断这些id对应的采购单是否都是新建、已分配状态
        List<Long> idList = listByIds(ids).stream().filter(purchaseEntity -> {
            int status = purchaseEntity.getStatus();
            return status == WareConstant.PurchaseStatus.ASSIGNED.getStatus();
        }).map(PurchaseEntity::getId).toList();
        if (idList.isEmpty()) {
            return;
        }

        //1. 修改采购单的状态
        this.lambdaUpdate()
                .set(PurchaseEntity::getStatus, WareConstant.PurchaseStatus.RECEIVED.getStatus())
                .set(PurchaseEntity::getUpdateTime, new Date())
                .in(PurchaseEntity::getId, idList)
                .update();

        //2. 修改采购单下所有需求的状态
        purchaseDetailDao.update(new UpdateWrapper<PurchaseDetailEntity>()
                .set("status", WareConstant.PurchaseDetailStatus.BUYING.getStatus())
                .in("purchase_id", idList));
    }
}