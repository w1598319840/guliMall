package cn.wjk.gulimall.cart.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.cart.domain.vo
 * @ClassName: CartVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午7:57
 * @Description: 一个用户的整个购物车
 */
@SuppressWarnings("unused")
public class CartVO {
    @Setter
    @Getter
    private List<CartItemVO> items;
    /**
     * 商品总数量
     */
    private Integer countNum;
    /**
     * 商品种类数量
     */
    private Integer countType;
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    /**
     * 优惠价格
     */
    @Setter
    @Getter
    private BigDecimal reduce = BigDecimal.ZERO;

    public Integer getCountNum() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream().map(CartItemVO::getCount).reduce(0, Integer::sum);
    }

    public Integer getCountType() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.size();
    }

    public BigDecimal getTotalAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream().filter(CartItemVO::getCheck)
                .map(CartItemVO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(reduce);
    }
}
