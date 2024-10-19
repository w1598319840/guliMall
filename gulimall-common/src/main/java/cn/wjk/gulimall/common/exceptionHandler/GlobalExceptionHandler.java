package cn.wjk.gulimall.common.exceptionHandler;

import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.BIZException;
import cn.wjk.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Package: cn.wjk.gulimall.common.exceptionHandler
 * @ClassName: GlobalExceptionHandler
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:53
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 数据校验异常处理器
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出错:{}, 异常类型:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError -> {
                    if (fieldError.getDefaultMessage() == null) {
                        return "该字段的数据不合法";
                    }
                    return fieldError.getDefaultMessage();
                }, (firstKey, secondKey) -> firstKey + "并且" + secondKey));
        BizHttpStatusEnum validException = BizHttpStatusEnum.VALID_EXCEPTION;
        return R.error(validException.getCode(), validException.getDesc()).put("data", map);
    }

    /**
     * 业务异常处理器
     */
    @ExceptionHandler(BIZException.class)
    public R handleBIZException(BIZException e) {
        log.error(e.getMessage(), e);
        BizHttpStatusEnum bizHttpStatus = e.getBizHttpStatusEnum();
        return R.error(bizHttpStatus.getCode(), bizHttpStatus.getDesc());
    }

    /**
     * 其他异常处理器
     */
    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        BizHttpStatusEnum unknownException = BizHttpStatusEnum.UNKNOWN_EXCEPTION;
        return R.error(unknownException.getCode(), unknownException.getDesc());
    }
}