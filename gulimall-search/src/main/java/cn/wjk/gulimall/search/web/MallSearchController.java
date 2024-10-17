package cn.wjk.gulimall.search.web;

import cn.wjk.gulimall.common.domain.vo.SearchVO;
import cn.wjk.gulimall.search.domain.dto.SearchDTO;
import cn.wjk.gulimall.search.service.MallSearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Package: cn.wjk.gulimall.search.web
 * @ClassName: MallSearchController
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 下午3:57
 * @Description:
 */
@Controller
@RequiredArgsConstructor
public class MallSearchController {
    private final MallSearchService mallSearchService;

    @GetMapping({"/list.html"})
    public ModelAndView index(SearchDTO searchDTO, ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName("list");
        SearchVO result = mallSearchService.search(searchDTO, request);
        modelAndView.addObject("result", result);
        return modelAndView;
    }
}
