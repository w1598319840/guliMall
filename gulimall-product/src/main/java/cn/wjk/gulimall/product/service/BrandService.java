package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.entity.BrandEntity;
import cn.wjk.gulimall.product.domain.vo.BrandVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 根据分类id获取该分类关联的品牌
     */
    List<BrandVO> brandsList(Long catId);
}

