package cn.wjk.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:45:16
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

