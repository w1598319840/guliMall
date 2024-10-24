package cn.wjk.gulimall.cart.service.impl;

import cn.wjk.gulimall.cart.domain.vo.CartItemVO;
import cn.wjk.gulimall.cart.domain.vo.CartVO;
import cn.wjk.gulimall.cart.service.CartService;
import cn.wjk.gulimall.common.constant.RedisConstants;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Package: cn.wjk.gulimall.cart.service.impl
 * @ClassName: CartServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午8:28
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void addCartItem(CartItemVO cartItemVO) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        //如何区分未登录的用户
        //使用cookie
//        String key = memberVO != null ?
//                RedisConstants.CART_LOGIN_PREFIX + memberVO.getId() : RedisConstants.CART_LOGOUT_PREFIX;

    }

    @Override
    public CartVO getCart(MemberVO memberVO) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        //如何区分未登录的用户
        //使用cookie
        String key = memberVO != null ?
                RedisConstants.CART_LOGIN_PREFIX + memberVO.getId() : RedisConstants.CART_LOGOUT_PREFIX;

        return null;
    }
}
