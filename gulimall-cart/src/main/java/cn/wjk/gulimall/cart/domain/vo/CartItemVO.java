package cn.wjk.gulimall.cart.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Package: cn.wjk.gulimall.cart.domain.vo
 * @ClassName: CartItemVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午7:58
 * @Description: 购物车中每一项商品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVO {
    private Long skuId;
    private String title;
    private Boolean check = true;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(count));
    }
}
