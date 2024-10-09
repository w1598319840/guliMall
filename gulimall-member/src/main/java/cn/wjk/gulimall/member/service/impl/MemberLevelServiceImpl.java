package cn.wjk.gulimall.member.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.member.dao.MemberLevelDao;
import cn.wjk.gulimall.member.entity.MemberLevelEntity;
import cn.wjk.gulimall.member.service.MemberLevelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(PageDTO pageDTO) {
        Page<MemberLevelEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        QueryWrapper<MemberLevelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(pageDTO.getSidx() != null, isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(key != null, wrapper ->
                wrapper.eq("name", key)
                        .or()
                        .like("name", key)
                        .or()
                        .like("note", key));
        page(page, queryWrapper);

        return new PageUtils(page);
    }
}