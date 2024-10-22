/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * https://www.renren.io
 * 版权所有，侵权必究！
 */

package cn.wjk.gulimall.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.http.HttpStatus;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    /**
     * 将对象转为json字符串，用于远程调用时进行网络传输
     */
    public R putJson(String key, Object value) {
        super.put(key, JSON.toJSONString(value));
        return this;
    }

    public <T> T getAndParse(String key, Class<T> type) {
        if (!(get(key) instanceof String)) {
            return null;
        }
        return JSON.parseObject((String) get(key), type);
    }

    public<T> T getAndParse(String key, TypeReference<T> type) {
        if (!(get(key) instanceof String)) {
            return null;
        }
        return JSON.parseObject((String) get(key), type);
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public int getCode() {
        return (int) get("code");
    }
}
