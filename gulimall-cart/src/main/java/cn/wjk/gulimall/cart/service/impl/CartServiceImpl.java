package cn.wjk.gulimall.cart.service.impl;

import cn.wjk.gulimall.cart.domain.vo.CartItemVO;
import cn.wjk.gulimall.cart.domain.vo.CartVO;
import cn.wjk.gulimall.cart.service.CartService;
import cn.wjk.gulimall.common.constant.RedisConstants;
import cn.wjk.gulimall.common.domain.dto.UserInfoDTO;
import cn.wjk.gulimall.common.domain.entity.SkuInfoEntity;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import cn.wjk.gulimall.common.enumeration.BizHttpStatusEnum;
import cn.wjk.gulimall.common.exception.CartException;
import cn.wjk.gulimall.common.exception.RPCException;
import cn.wjk.gulimall.common.feign.ProductFeign;
import cn.wjk.gulimall.common.utils.R;
import cn.wjk.gulimall.common.utils.ThreadLocalUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Package: cn.wjk.gulimall.cart.service.impl
 * @ClassName: CartServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午8:28
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ProductFeign productFeign;
    private final ThreadPoolExecutor executor;

    @Override
    public void addCartItem(Long skuId, Integer num) {
        //检查购物车中是否已经存在当前商品了
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        UserInfoDTO userInfoDTO = ThreadLocalUtils.get();
        String key = userInfoDTO.getUserId() == null ?
                RedisConstants.CART_LOGOUT_PREFIX + userInfoDTO.getUserKey() : //未登录
                RedisConstants.CART_LOGIN_PREFIX + userInfoDTO.getUserId();  //已登录
        CartItemVO cartItemVO = JSON.parseObject((String) hashOps.get(key, skuId.toString()), CartItemVO.class);
        if (cartItemVO != null) {
            //存在
            cartItemVO.setCount(cartItemVO.getCount() + num);
        } else {
            //不存在
            CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
                R result = productFeign.skuInfo(skuId);
                if (result.getCode() != 0) {
                    throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
                }
                return result.getAndParse("data", SkuInfoEntity.class);
            }, executor);
            CompletableFuture<List<String>> skuAttrFuture = CompletableFuture.supplyAsync(() -> {
                R result = productFeign.listWithString(skuId);
                if (result.getCode() != 0) {
                    throw new RPCException(BizHttpStatusEnum.RPC_EXCEPTION);
                }
                return result.getAndParse("data", new TypeReference<>() {
                });
            }, executor);

            cartItemVO = new CartItemVO();
            cartItemVO.setSkuId(skuId);
            cartItemVO.setCount(num);
            SkuInfoEntity skuInfo;
            try {
                skuInfo = skuInfoFuture.get();
                cartItemVO.setImage(skuInfo.getSkuDefaultImg());
                cartItemVO.setPrice(skuInfo.getPrice());
                cartItemVO.setTitle(skuInfo.getSkuTitle());

                List<String> skuAttr = skuAttrFuture.get();
                cartItemVO.setSkuAttr(skuAttr);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                throw new CartException(BizHttpStatusEnum.ADD_ITEM_EXCEPTION);
            }
        }

        //存入redis
        hashOps.put(key, skuId.toString(), JSON.toJSONString(cartItemVO));
        stringRedisTemplate.expire(key, Duration.ofDays(30));
    }

    @Override
    public CartVO getCart(MemberVO memberVO) {

        return null;
    }

    @Override
    public CartItemVO getCartItem(Long skuId) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        UserInfoDTO userInfoDTO = ThreadLocalUtils.get();
        String key = userInfoDTO.getUserId() == null ?
                RedisConstants.CART_LOGOUT_PREFIX + userInfoDTO.getUserKey() ://未登录
                RedisConstants.CART_LOGIN_PREFIX + userInfoDTO.getUserId();//已登录
        return JSON.parseObject((String) hashOps.get(key, skuId.toString()), CartItemVO.class);
    }
}
