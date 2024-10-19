package cn.wjk.gulimall.authservice.service.impl;

import cn.wjk.gulimall.authservice.service.AuthService;
import cn.wjk.gulimall.common.constant.RedisConstants;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.ObtainVerificationCodeException;
import cn.wjk.gulimall.common.exception.RPCException;
import cn.wjk.gulimall.common.feign.ThirdPartyFeign;
import cn.wjk.gulimall.common.utils.R;
import lombok.RequiredArgsConstructor;
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
public class AuthServiceImpl implements AuthService {
    private final ThirdPartyFeign thirdPartyFeign;
    private final StringRedisTemplate stringRedisTemplate;

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
}
