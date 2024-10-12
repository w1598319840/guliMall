package cn.wjk.gulimall.product.service.impl;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.product.dao.ProductAttrValueDao;
import cn.wjk.gulimall.product.domain.dto.UpdateSpuAttrDTO;
import cn.wjk.gulimall.product.domain.entity.ProductAttrValueEntity;
import cn.wjk.gulimall.product.service.ProductAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> listForSpu(Long spuId) {
        return this.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
    }

    @Override
    @Transactional
    public void updateSpuAttr(Long spuId, List<UpdateSpuAttrDTO> updateSpuAttrs) {
        //先删后增
        this.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        //后增
        List<ProductAttrValueEntity> productAttrValueEntities = updateSpuAttrs.stream().map(updateSpuAttrDTO -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuId);
            BeanUtils.copyProperties(updateSpuAttrDTO, productAttrValueEntity);
            return productAttrValueEntity;
        }).toList();

        this.saveBatch(productAttrValueEntities);
    }
}