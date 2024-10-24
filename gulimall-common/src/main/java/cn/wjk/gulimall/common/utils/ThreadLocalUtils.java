package cn.wjk.gulimall.common.utils;

import cn.wjk.gulimall.common.domain.dto.UserInfoDTO;

/**
 * @Package: cn.wjk.gulimall.common.utils
 * @ClassName: ThreadLocalUtils
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午9:38
 * @Description:
 */
public class ThreadLocalUtils {
    private ThreadLocalUtils() {

    }

    private static final ThreadLocal<UserInfoDTO> threadLocal = new ThreadLocal<>();

    public static UserInfoDTO get() {
        return threadLocal.get();
    }

    public static void set(UserInfoDTO value) {
        threadLocal.set(value);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
