package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.product.domain.entity.SpuImagesEntity;

import java.util.Map;

/**
 * spu图片
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

