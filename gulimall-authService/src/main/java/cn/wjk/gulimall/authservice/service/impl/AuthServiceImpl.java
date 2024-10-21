package cn.wjk.gulimall.authservice.service.impl;

import cn.wjk.gulimall.authservice.domain.dto.UserRegisterDTO;
import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.constant.RedisConstants;
import cn.wjk.gulimall.common.domain.dto.GithubOAuthDTO;
import cn.wjk.gulimall.common.domain.dto.UserLoginDTO;
import cn.wjk.gulimall.common.domain.entity.MemberEntity;
import cn.wjk.gulimall.common.domain.to.UserRegisterTO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.LoginException;
import cn.wjk.gulimall.common.exception.ObtainVerificationCodeException;
import cn.wjk.gulimall.common.exception.RPCException;
import cn.wjk.gulimall.common.exception.RegisterException;
import cn.wjk.gulimall.common.feign.MemberFeign;
import cn.wjk.gulimall.common.feign.ThirdPartyFeign;
import cn.wjk.gulimall.common.utils.R;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

/**
 * @Package: cn.wjk.gulimall.authservice.service.impl
 * @ClassName: AuthServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:15
 * @Description:
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final ThirdPartyFeign thirdPartyFeign;
    private final StringRedisTemplate stringRedisTemplate;
    private final MemberFeign memberFeign;

    @Override
    public void sendCode(String phone) {
        ValueOperations<String, String> stringOps = stringRedisTemplate.opsForValue();
        String key = RedisConstants.AUTH_LOGIN_CODE_PREFIX + phone;
        //一个用户每60s只能发送一次验证码
        String redisCode = stringOps.get(key);
        if (redisCode != null) {
            //redis中已经存在当前用户的验证码了，要判断是否到可再次请求的时间了
            long lastTime = Long.parseLong(redisCode.split("_")[1]);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime <= 60_000) {
                throw new ObtainVerificationCodeException(BizHttpStatusEnum.SMS_CODE_EXCEPTION);
            }
        }

        String code = String.valueOf(new Random().nextInt(100_000, 999_999));
        //远程调用
        R result = thirdPartyFeign.sendCode(phone, code);
        if (result.getCode() != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        //存入Redis(同时要存入防刷时间)
        stringOps.set(key,
                code + "_" + System.currentTimeMillis(), Duration.ofMinutes(3L));
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        ValueOperations<String, String> stringOps = stringRedisTemplate.opsForValue();
        String key = RedisConstants.AUTH_LOGIN_CODE_PREFIX + userRegisterDTO.getPhone();
        //判断验证码
        String redisCode = stringOps.get(key);
        if (redisCode == null || !userRegisterDTO.getCode().equals(redisCode.split("_")[0])) {
            throw new RegisterException(BizHttpStatusEnum.ERROR_CODE_EXCEPTION);
        }
        stringRedisTemplate.delete(key);

        //开始注册
        UserRegisterTO userRegisterTO = new UserRegisterTO();
        BeanUtils.copyProperties(userRegisterDTO, userRegisterTO);
        R result = memberFeign.register(userRegisterTO);
        int code = result.getCode();
        if (code == BizHttpStatusEnum.PHONE_ALREADY_USED_EXCEPTION.getCode()) {
            throw new RegisterException(BizHttpStatusEnum.PHONE_ALREADY_USED_EXCEPTION);
        } else if (code == BizHttpStatusEnum.USERNAME_ALREADY_EXIST_EXCEPTION.getCode()) {
            throw new RegisterException(BizHttpStatusEnum.USERNAME_ALREADY_EXIST_EXCEPTION);
        } else if (code != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
    }

    @Override
    public void login(UserLoginDTO userLoginDTO) {
        R result = memberFeign.login(userLoginDTO);
        int code = result.getCode();
        if (code == BizHttpStatusEnum.LOGIN_EXCEPTION.getCode()) {
            throw new LoginException(BizHttpStatusEnum.LOGIN_EXCEPTION);
        } else if (code != 0) {
            throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
    }

    @Override
    public MemberEntity githubOAuth(String code) {
        R result = thirdPartyFeign.getGithubOAuthAccessKey(code);
        if (result.getCode() != 0) {
            throw new LoginException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        GithubOAuthDTO githubOAuthDTO = JSON.parseObject((String) result.get("data"), GithubOAuthDTO.class);
        //使用access_token
        //1. 使用当前社交帐号登录
        result = memberFeign.login(githubOAuthDTO);
        if (result.getCode() != 0) {
            throw new LoginException(BizHttpStatusEnum.RPC_EXCEPTION);
        }
        return JSON.parseObject(((String) result.get("data")), MemberEntity.class);
    }
}
