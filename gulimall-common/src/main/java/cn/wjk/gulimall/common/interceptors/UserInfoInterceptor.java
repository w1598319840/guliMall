package cn.wjk.gulimall.common.interceptors;

import cn.wjk.gulimall.common.constant.AuthConstants;
import cn.wjk.gulimall.common.domain.dto.UserInfoDTO;
import cn.wjk.gulimall.common.domain.vo.MemberVO;
import cn.wjk.gulimall.common.utils.ThreadLocalUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Package: cn.wjk.gulimall.common.interceptors
 * @ClassName: UserInfoInterceptor
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午9:36
 * @Description: 用户信息拦截器
 */
@Component
@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        MemberVO memberVO = (MemberVO) request.getSession().getAttribute(AuthConstants.LOGIN_USER);
        if (memberVO != null) {
            //用户已登录
            userInfoDTO.setUserId(memberVO.getId());
        }
        //无论怎么样都需要添加一个临时用户
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray == null) {
            cookieArray = new Cookie[0];
        }
        List<Cookie> cookies = Arrays.stream(cookieArray)
                .filter(cookie -> Objects.equals(cookie.getName(), AuthConstants.TEMP_USER_KEY)).toList();
        String userKey;
        if (cookies.isEmpty()) {
            //临时用户未创立
            String uuid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(AuthConstants.TEMP_USER_KEY, uuid);
            cookie.setMaxAge(AuthConstants.TEMP_USER_KEY_TTL);
            cookie.setDomain("gulimall.com");
            response.addCookie(cookie);
            userKey = uuid;
        } else {
            userKey = cookies.getFirst().getValue();
        }
        userInfoDTO.setUserKey(userKey);

        ThreadLocalUtils.set(userInfoDTO);
        return true;
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request,
                           @Nonnull HttpServletResponse response,
                           @Nonnull Object handler,
                           ModelAndView modelAndView) {
        ThreadLocalUtils.remove();
    }
}
