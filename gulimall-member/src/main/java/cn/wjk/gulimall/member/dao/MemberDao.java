package cn.wjk.gulimall.member.dao;

import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:45:16
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
