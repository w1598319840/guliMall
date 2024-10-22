package cn.wjk.gulimall.member.service.impl;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.GithubUserDTO;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.LoginException;
import cn.wjk.gulimall.common.exception.RegisterException;
import cn.wjk.gulimall.common.feign.ThirdPartyFeign;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.member.dao.MemberDao;
import cn.wjk.gulimall.member.dao.MemberLevelDao;
import cn.wjk.gulimall.member.entity.MemberLevelEntity;
import cn.wjk.gulimall.member.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    private final MemberLevelDao memberLevelDao;
    private final ThirdPartyFeign thirdPartyFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(UserRegisterTO userRegisterTO) throws RegisterException {
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
        memberEntity.setNickname(userRegisterTO.getUsername());
        memberEntity.setLevelId(memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1)).getId());
        memberEntity.setCreateTime(new Date());

        //密码加密存储
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        memberEntity.setPassword(bCryptPasswordEncoder.encode(userRegisterTO.getPassword()));
        this.save(memberEntity);
    }

    @Override
    public MemberVO login(UserLoginDTO userLoginDTO) throws LoginException {
        String loginacct = userLoginDTO.getLoginacct();
        MemberEntity memberEntity = this.lambdaQuery().eq(MemberEntity::getMobile, loginacct)
                .or().eq(MemberEntity::getUsername, loginacct).one();
        if (memberEntity == null) {
            throw new LoginException(BizHttpStatusEnum.LOGIN_EXCEPTION);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), memberEntity.getPassword())) {
            throw new LoginException(BizHttpStatusEnum.LOGIN_EXCEPTION);
        }
        MemberVO memberVO = new MemberVO();
        BeanUtils.copyProperties(memberEntity, memberVO);
        return memberVO;
    }

    @Override
    public MemberVO login(GithubOAuthDTO githubOAuthDTO) {
        //1. 根据access_key获取用户信息
        R result = thirdPartyFeign.getGithubUserInfo(githubOAuthDTO.getAccess_token());
        if (result.getCode() != 0) {
            throw new LoginException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        GithubUserDTO githubUserDTO = result.getAndParse("data", GithubUserDTO.class);

        //2. 判断是否注册过
        MemberEntity memberEntity = this.lambdaQuery().eq(MemberEntity::getGithubUid, githubUserDTO.getId()).one();
        if (memberEntity == null) {
            memberEntity = socialRegister(githubUserDTO);
        }
        MemberVO memberVO = new MemberVO();
        BeanUtils.copyProperties(memberEntity, memberVO);
        return memberVO;
    }

    /**
     * 使用github进行社交注册
     */
    private MemberEntity socialRegister(GithubUserDTO githubUserDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setLevelId(memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1)).getId());
        //username不能设置，不然可能会发生username重复的现象
//        memberEntity.setUsername(githubUserDTO.getLogin());
        //默认密码123456
        memberEntity.setPassword(new BCryptPasswordEncoder().encode("123456"));
        memberEntity.setNickname(githubUserDTO.getName());
        memberEntity.setHeader(githubUserDTO.getAvatar_url());
        memberEntity.setCreateTime(new Date());
        memberEntity.setGithubUid(githubUserDTO.getId());
        this.save(memberEntity);
        return memberEntity;
    }
}