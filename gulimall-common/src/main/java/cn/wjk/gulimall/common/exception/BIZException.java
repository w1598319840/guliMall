package cn.wjk.gulimall.common.exception;

/**
 * @Package: cn.wjk.gulimall.common.exception
 * @ClassName: BIZException
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午4:56
 * @Description: 有关业务的异常
 */
public class BIZException extends RuntimeException {
    public BIZException(String message) {
        super(message);
    }
}