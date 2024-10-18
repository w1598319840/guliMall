package cn.wjk.gulimall.product.web;

import cn.wjk.gulimall.product.domain.vo.SkuItemVO;
import cn.wjk.gulimall.product.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Package: cn.wjk.gulimall.product.web
 * @ClassName: ItemController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/17 下午7:56
 * @Description: 商品详情页面的controller
 */
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * 查询某一sku的商品详细信息
     */
    @GetMapping("/{skuId}.html")
    public ModelAndView item(@PathVariable("skuId") Long skuId) {
        ModelAndView modelAndView = new ModelAndView("item");
        SkuItemVO skuItemVO = itemService.getItemDetail(skuId);
        modelAndView.addObject("item", skuItemVO);
        return modelAndView;
    }
}
