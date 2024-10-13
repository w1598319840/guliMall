package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: RPCException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午8:35
 * @Description:
 */
public class RPCException extends BIZException {
    public RPCException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
