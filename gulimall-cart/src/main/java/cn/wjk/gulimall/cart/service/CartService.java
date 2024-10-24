package cn.wjk.gulimall.cart.service;

import cn.wjk.gulimall.cart.domain.vo.CartItemVO;
import cn.wjk.gulimall.cart.domain.vo.CartVO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;

/**
 * @Package: cn.wjk.gulimall.cart.service
 * @ClassName: CartService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午8:28
 * @Description:
 */
public interface CartService {
    /**
     * 添加购物车项
     */
    void addCartItem(CartItemVO cartItemVO);

    /**
     * 获取购物车详情
     */
    CartVO getCart(MemberVO memberVO);
}
