package cn.wjk.gulimall.common.exception;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: BIZException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午4:56
 * @Description: 有关业务的异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BIZException extends RuntimeException {
    BizHttpStatusEnum bizHttpStatusEnum;

    public BIZException(BizHttpStatusEnum bizHttpStatusEnum) {
        this.bizHttpStatusEnum = bizHttpStatusEnum;
    }
}
