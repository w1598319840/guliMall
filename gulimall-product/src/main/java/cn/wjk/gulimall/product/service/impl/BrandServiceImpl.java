package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.dao.BrandDao;
import cn.wjk.gulimall.product.domain.entity.BrandEntity;
import cn.wjk.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(PageDTO pageDTO) {
        String key = pageDTO.getKey();
        Page<BrandEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        lambdaQuery()
                .eq(key != null, BrandEntity::getBrandId, key)
                .or()
                .like(key != null, BrandEntity::getName, key)
                .page(page);

        return new PageUtils(page);
    }

}