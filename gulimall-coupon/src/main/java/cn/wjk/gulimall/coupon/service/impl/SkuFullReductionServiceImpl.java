package cn.wjk.gulimall.coupon.service.impl;

import cn.wjk.gulimall.common.domain.dto.MemberPriceDTO;
import cn.wjk.gulimall.common.domain.to.SkuReductionTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.coupon.dao.MemberPriceDao;
import cn.wjk.gulimall.coupon.dao.SkuFullReductionDao;
import cn.wjk.gulimall.coupon.dao.SkuLadderDao;
import cn.wjk.gulimall.coupon.entity.MemberPriceEntity;
import cn.wjk.gulimall.coupon.entity.SkuFullReductionEntity;
import cn.wjk.gulimall.coupon.entity.SkuLadderEntity;
import cn.wjk.gulimall.coupon.service.SkuFullReductionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("skuFullReductionService")
@RequiredArgsConstructor
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    private final SkuLadderDao skuLadderDao;
    private final SkuFullReductionDao skuFullReductionDao;
    private final MemberPriceDao memberPriceDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSkuReduction(List<SkuReductionTO> skuReductionTOs) {
        //sms_sku_ladder
        saveSkuLadder(skuReductionTOs);

        //sms_sku_full_reduction
        saveSkuFullReduction(skuReductionTOs);

        //sms_member_price
        saveMemberPrice(skuReductionTOs);
    }

    /**
     * 保存
     */
    private void saveMemberPrice(List<SkuReductionTO> skuReductionTOs) {
        List<MemberPriceEntity> memberPriceEntities = new ArrayList<>();
        for (SkuReductionTO skuReductionTO : skuReductionTOs) {
            Long skuId = skuReductionTO.getSkuId();
            List<MemberPriceDTO> memberPriceList = skuReductionTO.getMemberPrice();
            if (memberPriceList == null || memberPriceList.isEmpty()) {
                continue;
            }
            List<MemberPriceEntity> memberPriceEntitiyList = memberPriceList.stream().map(memberPrice -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuId);
                memberPriceEntity.setMemberLevelId(memberPrice.getId());
                memberPriceEntity.setMemberLevelName(memberPrice.getName());
                memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(memberPriceEntity -> memberPriceEntity.getMemberPrice().compareTo(BigDecimal.ZERO) > 0).toList();
            memberPriceEntities.addAll(memberPriceEntitiyList);
        }
        memberPriceDao.insert(memberPriceEntities);
    }

    /**
     * 保存商品满减信息
     */
    private void saveSkuFullReduction(List<SkuReductionTO> skuReductionTOs) {
        List<SkuFullReductionEntity> skuFullReductionEntities = skuReductionTOs.stream().map(skuReductionTO -> {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            BeanUtils.copyProperties(skuReductionTO, skuFullReductionEntity);
            //该表的add_other对应skuReduction中的priceStatus
            skuFullReductionEntity.setAddOther(skuReductionTO.getPriceStatus());
            return skuFullReductionEntity;
        }).filter(skuFullReductionEntity -> skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) > 0
                && skuFullReductionEntity.getReducePrice().compareTo(BigDecimal.ZERO) > 0).toList();
        skuFullReductionDao.insert(skuFullReductionEntities);
    }

    /**
     * 保存商品价格阶梯
     */
    private void saveSkuLadder(List<SkuReductionTO> skuReductionTOs) {
        List<SkuLadderEntity> skuLadderEntities = skuReductionTOs.stream().map(skuReductionTO -> {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
            //该表的add_other对应skuReduction中的countStatus
            skuLadderEntity.setAddOther(skuReductionTO.getCountStatus());
            return skuLadderEntity;
        }).filter(skuLadderEntity -> skuLadderEntity.getFullCount() > 0 &&
                (skuLadderEntity.getDiscount().compareTo(BigDecimal.ZERO) > 0)).toList();
        skuLadderDao.insert(skuLadderEntities);
    }
}