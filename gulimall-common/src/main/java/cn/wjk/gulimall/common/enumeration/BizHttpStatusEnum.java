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
 *               11: 优惠券模块
 *               12: 会员模块
 *               13: 订单模块
 *               14: 商品模块
 *               15: 仓储模块
 *               16: 权限认证模块
 *               17: 购物车模块
 */
@AllArgsConstructor
@Getter
public enum BizHttpStatusEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    ILLEGAL_PARAMETERS_EXCEPTION(10002, "传入的参数不合法"),
    RPC_EXCEPTION(10003, "远程调用服务失败"),
    RPC_DATA_EXCEPTION(10004, "远程获取数据与预期数据类型不符"),
    PRODUCT_UP_EXCEPTION(14001, "商品上架失败"),
    MERGE_EXCEPTION(15001, "仅能合并新建、已分配状态的采购需求"),
    PURCHASE_STATUS_EXCEPTION(15002, "采购单状态异常"),
    SMS_CODE_EXCEPTION(16001, "获取验证码速度过快"),
    ERROR_CODE_EXCEPTION(16002, "错误的验证码"),
    PHONE_ALREADY_USED_EXCEPTION(16003, "当前手机号已被注册"),
    USERNAME_ALREADY_EXIST_EXCEPTION(16004, "当前用户名已被占用"),
    LOGIN_EXCEPTION(16005, "登录账号与密码不匹配"),
    OAUTH_LOGIN_EXCEPTION(16006, "使用社交账号登录失败，请稍后再试"),
    ADD_ITEM_EXCEPTION(17001,"添加商品进入购物车失败，请稍后再试");

    private final int code;
    private final String desc;
}
