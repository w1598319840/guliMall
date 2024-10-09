package cn.wjk.gulimall.member.service;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.member.entity.MemberLevelEntity;

/**
 * 会员等级
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:45:16
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(PageDTO pageDTO);
}

