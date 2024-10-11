package cn.wjk.gulimall.ware.exceptionHandler;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.BIZException;
import cn.wjk.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Package: cn.wjk.gulimall.ware.exceptionHandler
 * @ClassName: GlobalExceptionHandler
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午4:57
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BIZException.class)
    public R handleBIZException(BIZException e) {
        log.error(e.getMessage(), e);
        BizHttpStatusEnum bizHttpStatus = e.getBizHttpStatusEnum();
        return R.error(bizHttpStatus.getCode(), bizHttpStatus.getDesc());
    }
}
