package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.constant.WareConstant;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.PurchaseException;
import cn.wjk.gulimall.common.exception.RPCException;
import cn.wjk.gulimall.common.feign.ProductFeign;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.ware.dao.PurchaseDao;
import cn.wjk.gulimall.ware.dao.PurchaseDetailDao;
import cn.wjk.gulimall.ware.dao.WareSkuDao;
import cn.wjk.gulimall.ware.domain.dto.PurchaseDoneDTO;
import cn.wjk.gulimall.ware.domain.dto.PurchaseItemDTO;
import cn.wjk.gulimall.ware.domain.dto.WareMergeDTO;
import cn.wjk.gulimall.ware.domain.entity.PurchaseDetailEntity;
import cn.wjk.gulimall.ware.domain.entity.PurchaseEntity;
import cn.wjk.gulimall.ware.domain.entity.WareSkuEntity;
import cn.wjk.gulimall.ware.service.PurchaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("purchaseService")
@RequiredArgsConstructor
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailDao purchaseDetailDao;
    private final WareSkuDao wareSkuDao;
    private final PurchaseDetailServiceImpl purchaseDetailService;
    private final ProductFeign productFeign;

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
            throw new PurchaseException(BizHttpStatusEnum.MERGE_EXCEPTION);
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

    @Override
    @Transactional
    public void purchaseDone(PurchaseDoneDTO purchaseDoneDTO) {
        //-1. 判断当前采购单是否已经采购过了
        Long purchaseId = purchaseDoneDTO.getId();
//        Integer status = this.getById(purchaseId).getStatus();
//        WareConstant.PurchaseStatus received = WareConstant.PurchaseStatus.RECEIVED;
//        if (status != received.getStatus()) {
//            throw new PurchaseException(BizHttpStatusEnum.PURCHASE_STATUS_EXCEPTION);
//        }


        //0. 判断是否存在需求商品采购失败
        int purchaseStatus = WareConstant.PurchaseStatus.FINISH.getStatus();
        List<PurchaseItemDTO> items = purchaseDoneDTO.getItems();
        if (items.isEmpty()) {
            return;
        }
        List<PurchaseItemDTO> errorList = items.stream().filter(purchaseItemDTO ->
                purchaseItemDTO.getStatus() == WareConstant.PurchaseDetailStatus.ERROR.getStatus()
        ).toList();
        if (!errorList.isEmpty()) {
            purchaseStatus = WareConstant.PurchaseStatus.ERROR.getStatus();
        }

        //1. 修改purchaseDetail中的信息
        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemDTO item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setStatus(item.getStatus());
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updates);

        //2. 修改purchase中的信息
        this.lambdaUpdate().set(PurchaseEntity::getStatus, purchaseStatus)
                .set(PurchaseEntity::getUpdateTime, new Date())
                .eq(PurchaseEntity::getId, purchaseId)
                .update();

        //3. 采购成功的商品进行入库处理
        addStock(purchaseDoneDTO);

    }

    /**
     * 入库处理
     */
    private void addStock(PurchaseDoneDTO purchaseDoneDTO) {
        //要选出采购成功的商品
        List<PurchaseItemDTO> successPurchaseItems = purchaseDoneDTO.getItems().stream().filter(purchaseItemDTO -> {
            int status = purchaseItemDTO.getStatus();
            return status == WareConstant.PurchaseDetailStatus.FINISH.getStatus();
        }).toList();
        //准备库存信息
        //每个purchaseDetail的skuId和purchaseDetailEntity映射关系
        List<Long> purchaseDetailIds = successPurchaseItems.stream().map(PurchaseItemDTO::getItemId).toList();
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailDao.selectBatchIds(purchaseDetailIds);

        //远程调用获取skuId和SkuName的映射关系
        List<Long> skuIds = purchaseDetailEntities.stream().map(PurchaseDetailEntity::getSkuId).toList();
        R result = productFeign.getSkuNamesBySkuIds(skuIds);
        if (result.getCode() != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        Object data = result.get("data");
        if (!(data instanceof Map<?, ?>)) {
            throw new RPCException(BizHttpStatusEnum.RPC_DATA_EXCEPTION);
        }
        //经过网络流后，HashMap -> LinkedHashMap, 最重要的是，其中Long类型的key变成了String类型，
        //如果直接转换为Map<Long, String>，那么实际上的key的类型就会变成byte，真是头疼
        @SuppressWarnings("all")
        Map<String, String> skuIdToSkuNameRawMap = (Map<String, String>) data;
        HashMap<Long, String> skuIdToSkuNameMap = new HashMap<>();
        for (Map.Entry<String, String> entry : skuIdToSkuNameRawMap.entrySet()) {
            skuIdToSkuNameMap.put(Long.valueOf(entry.getKey()), entry.getValue());
        }

        //先要看看当前库存表中同一个仓库中的同一个sku是否已经存在了
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        for (PurchaseDetailEntity detailEntity : purchaseDetailEntities) {
            queryWrapper.or(wrapper ->
                    wrapper.eq("ware_id", detailEntity.getWareId())
                            .eq("sku_id", detailEntity.getSkuId()));
        }
        Map<Long, Long> skuIdToWareIdMap
                = wareSkuDao.selectList(queryWrapper).stream().collect(Collectors.toMap(
                WareSkuEntity::getSkuId, WareSkuEntity::getWareId
        ));

        //存在则修改
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        //不存在则插入
        List<PurchaseDetailEntity> inserts = new ArrayList<>();
        for (PurchaseDetailEntity detailEntity : purchaseDetailEntities) {
            if (skuIdToWareIdMap.containsKey(detailEntity.getSkuId()) &&
                    skuIdToWareIdMap.get(detailEntity.getSkuId()).equals(detailEntity.getWareId())) {
                //说明存在同仓库，同skuId的记录
                updates.add(detailEntity);
            } else {
                //不存在
                inserts.add(detailEntity);
            }
        }

        //insert
        List<WareSkuEntity> wareSkuEntitiesInsert = inserts.stream().map(detailEntity -> {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(detailEntity.getSkuId());
            wareSkuEntity.setWareId(detailEntity.getWareId());
            wareSkuEntity.setStock(detailEntity.getSkuNum());
            wareSkuEntity.setSkuName(skuIdToSkuNameMap.get(detailEntity.getSkuId()));
            wareSkuEntity.setStockLocked(0);
            return wareSkuEntity;
        }).toList();
        wareSkuDao.insert(wareSkuEntitiesInsert);

        //update(暂时想不出能够不使用循环的方式进行更新)
        //可以选择先删后增来避免循环更新
        for (PurchaseDetailEntity detailEntity : updates) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(detailEntity.getSkuId());
            wareSkuEntity.setWareId(detailEntity.getWareId());
            wareSkuEntity.setStock(detailEntity.getSkuNum());
            wareSkuDao.updateWareSkuStock(wareSkuEntity);
        }
    }
}