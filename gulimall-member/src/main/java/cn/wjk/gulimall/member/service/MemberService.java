package cn.wjk.gulimall.member.service;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import cn.wjk.gulimall.common.exception.LoginException;
import cn.wjk.gulimall.common.exception.RegisterException;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 会员
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:45:16
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册
     */
    void register(UserRegisterTO userRegisterTO) throws RegisterException;

    /**
     * 登录
     */
    MemberVO login(UserLoginDTO userLoginDTO) throws LoginException;

    /**
     * GitHub oauth 登录
     */
    MemberVO login(GithubOAuthDTO githubOAuthDTO);
}

