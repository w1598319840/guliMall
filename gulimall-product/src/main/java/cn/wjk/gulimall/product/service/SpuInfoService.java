package cn.wjk.gulimall.product.service;

import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.product.domain.dto.spuSaveDto.SpuSaveDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.product.domain.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveDTO spuSaveDTO);
}

