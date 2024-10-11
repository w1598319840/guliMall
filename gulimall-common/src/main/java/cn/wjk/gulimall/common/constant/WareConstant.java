package cn.wjk.gulimall.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Package: cn.wjk.gulimall.common.constant
 * @ClassName: WareConstant
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/11 下午3:12
 * @Description: 库存服务的常量类
 */
public class WareConstant {
    @AllArgsConstructor
    @Getter
    public enum PurchaseStatus {
        NEW(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVED(2, "已领取"),
        FINISH(3, "已完成"),
        ERROR(4, "出现异常");

        private final int status;
        private final String desc;
    }

    @AllArgsConstructor
    @Getter
    public enum PurchaseDetailStatus {
        NEW(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISH(3, "已完成"),
        ERROR(4, "采购失败");

        private final int status;
        private final String desc;
    }
}
