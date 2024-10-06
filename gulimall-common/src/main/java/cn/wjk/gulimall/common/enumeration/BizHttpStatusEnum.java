package cn.wjk.gulimall.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Package: cn.wjk.gulimall.common.enumeration
 * @ClassName: BizHttpStatusEnum
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/6 下午2:26
 * @Description: 业务状态码的枚举类
 *               该枚举类用于定义业务中出现的各种情况对应的状态码以及描述信息
 *               状态码的组成是这样的: 前两位表示状态码类型，后三位表示具体状态码信息
 *               10: 通用
 *               11: 商品模块
 *               12: 订单模块
 *               13: 购物车模块
 *               14: 物流模块
 */
@AllArgsConstructor
@Getter
public enum BizHttpStatusEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败");


    private final int code;
    private final String desc;
}
