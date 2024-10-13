package cn.wjk.gulimall.product.web;

import cn.wjk.gulimall.product.domain.entity.CategoryEntity;
import cn.wjk.gulimall.product.domain.vo.Catelog2VO;
import cn.wjk.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Package: cn.wjk.gulimall.product.web
 * @ClassName: IndexController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/13 下午2:07
 * @Description: 有关thymeleaf页面跳转的controller都放在web包下
 */
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final CategoryService categoryService;

    @GetMapping({"/index", "/"})
    public ModelAndView index(ModelAndView modelAndView) {
        //查询出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getAllRootCategories();
        modelAndView.setViewName("index");
        modelAndView.addObject("categories", categoryEntities);
        return modelAndView;
    }

    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatalogJson(ModelAndView modelAndView) {
        return categoryService.getCatalogJson();
    }
}
