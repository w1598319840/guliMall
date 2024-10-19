package cn.wjk.gulimall.member.service.impl;

import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.RegisterException;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.member.dao.MemberDao;
import cn.wjk.gulimall.member.dao.MemberLevelDao;
import cn.wjk.gulimall.member.entity.MemberEntity;
import cn.wjk.gulimall.member.entity.MemberLevelEntity;
import cn.wjk.gulimall.member.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    private final MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(UserRegisterTO userRegisterTO) throws RegisterException{
        //判断当前手机号是否注册过了
        Long count = this.lambdaQuery().eq(MemberEntity::getMobile, userRegisterTO.getPhone()).count();
        if (count > 0) {
            //被注册过了
            throw new RegisterException(BizHttpStatusEnum.PHONE_ALREADY_USED_EXCEPTION);
        }
        //判断当前用户名是否注册过了
        count = this.lambdaQuery().eq(MemberEntity::getUsername, userRegisterTO.getUsername()).count();
        if (count > 0) {
            throw new RegisterException(BizHttpStatusEnum.USERNAME_ALREADY_EXIST_EXCEPTION);
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMobile(userRegisterTO.getPhone());
        memberEntity.setUsername(userRegisterTO.getUsername());
        memberEntity.setLevelId(memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1)).getId());

        //密码加密存储
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        memberEntity.setPassword(bCryptPasswordEncoder.encode(userRegisterTO.getPassword()));
        this.save(memberEntity);
    }
}