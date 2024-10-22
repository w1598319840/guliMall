package cn.wjk.gulimall.member.controller;

import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.LoginException;
import cn.wjk.gulimall.common.exception.RegisterException;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 会员
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 13:45:16
 */
@RestController
@RequestMapping("/member/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R register(@RequestBody UserRegisterTO userRegisterTO) {
        try {
            memberService.register(userRegisterTO);
        } catch (RegisterException e) {
            BizHttpStatusEnum bizHttpStatusEnum = e.getBizHttpStatusEnum();
            return R.error(bizHttpStatusEnum.getCode(), bizHttpStatusEnum.getDesc());
        }
        return R.ok();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@RequestBody UserLoginDTO userLoginDTO) {
        MemberVO memberVO;
        try {
            memberVO = memberService.login(userLoginDTO);
        } catch (LoginException e) {
            BizHttpStatusEnum bizHttpStatusEnum = e.getBizHttpStatusEnum();
            return R.error(bizHttpStatusEnum.getCode(), bizHttpStatusEnum.getDesc());
        }
        return R.ok().putJson("data", memberVO);
    }

    @PostMapping("/oauth/github/login")
    public R login(@RequestBody GithubOAuthDTO githubOAuthDTO) {
        MemberVO memberVO = memberService.login(githubOAuthDTO);
        return R.ok().putJson("data", memberVO);
    }
}
