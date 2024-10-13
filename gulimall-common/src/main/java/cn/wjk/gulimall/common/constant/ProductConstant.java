package cn.wjk.gulimall.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Package: cn.wjk.gulimall.common.constant
 * @ClassName: ProductConstant
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/8 下午4:43
 * @Description: 商品系统的枚举类
 */
public interface ProductConstant {
    @AllArgsConstructor
    @Getter
    enum AttrType {
        ATTR_TYPE_BASE("base", 1),
        ATTR_TYPE_SALE("sale", 0);
        private final String type;
        private final int code;
    }

    @AllArgsConstructor
    @Getter
    enum SpuStatus {
        NEW(0, "新建"),
        UP(1, "上架"),
        DOWN(2, "下架");
        private final int code;
        private final String desc;
    }
}
