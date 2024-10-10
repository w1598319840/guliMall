package cn.wjk.gulimall.coupon.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.coupon.dao.SpuBoundsDao;
import cn.wjk.gulimall.coupon.entity.SpuBoundsEntity;
import cn.wjk.gulimall.coupon.service.SpuBoundsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}