package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: ObtainVerificationCodeException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:49
 * @Description: 获取验证码的异常
 */
public class ObtainVerificationCodeException extends BIZException {
    public ObtainVerificationCodeException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
