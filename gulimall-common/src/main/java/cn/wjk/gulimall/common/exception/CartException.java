package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: CartException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/24 下午1:38
 * @Description: 购物车功能的异常类
 */
public class CartException extends BIZException{
    public CartException(BizHttpStatusEnum bizHttpStatusEnum) {
        super(bizHttpStatusEnum);
    }
}
