package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.product.domain.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

