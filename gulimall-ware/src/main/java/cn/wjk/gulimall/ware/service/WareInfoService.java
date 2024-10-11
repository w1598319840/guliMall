package cn.wjk.gulimall.ware.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.ware.domain.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页查询
     */
    PageUtils pageQueryDetail(PageDTO pageDTO);
}

