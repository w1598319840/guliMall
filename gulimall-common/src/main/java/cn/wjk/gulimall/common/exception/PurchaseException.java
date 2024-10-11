package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: PurchaseException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午4:54
 * @Description:
 */
public class PurchaseException extends BIZException {
    public PurchaseException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
