package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.product.domain.entity.BrandEntity;

/**
 * 品牌
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(PageDTO pageDTO);

    /**
     * 由于逻辑外键的存在，我们在update的时候不能简单的更新一个表
     */
    void updateDetail(BrandEntity brand);
}

