package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: LoginException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/20 下午2:38
 * @Description: 登录异常
 */
public class LoginException extends BIZException {
    public LoginException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
