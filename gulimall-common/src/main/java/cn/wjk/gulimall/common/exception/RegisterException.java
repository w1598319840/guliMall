package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: RegisterException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午3:52
 * @Description: 注册异常
 */
public class RegisterException extends BIZException {
    public RegisterException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
