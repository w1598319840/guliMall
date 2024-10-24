package cn.wjk.gulimall.cart.web;

import cn.wjk.gulimall.cart.domain.vo.CartItemVO;
import cn.wjk.gulimall.cart.service.CartService;
import cn.wjk.gulimall.common.domain.dto.UserInfoDTO;
import cn.wjk.gulimall.common.utils.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @Package: cn.wjk.gulimall.cart.web
 * @ClassName: CartController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午8:29
 * @Description: 购物车
 */
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    public String success() {
        return "success";
    }

    /**
     * 跳转到购物车界面
     */
    @GetMapping("/cart.html")
    public ModelAndView cartListPage() {
        UserInfoDTO userInfoDTO = ThreadLocalUtils.get();
        ModelAndView modelAndView = new ModelAndView("cartList");
        return modelAndView;
    }

    /**
     * 添加购物车商品
     */
    @GetMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                              RedirectAttributes redirectAttributes) {
        cartService.addCartItem(skuId, num);
        //请求转发时模拟request域
        redirectAttributes.addAttribute("skuId", skuId);
        //请求转发时模拟session域
//        redirectAttributes.addFlashAttribute()
        return "redirect:http://cart.gulimall.com/addCartItemSuccess.html";
    }

    /**
     * 添加购物车商品成功后
     */
    @GetMapping("/addCartItemSuccess.html")
    public String addCartItemSuccess(@RequestParam("skuId") Long skuId, Model model) {
        CartItemVO cartItemVO = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVO);
        return "success";
    }
}
