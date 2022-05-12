package fit.wenchao.second_hand_trading_platform_front.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class MybatisPageArgumentResolver implements HandlerMethodArgumentResolver {
    //static @interface Page{
    //    Class<?> value();
    //}
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        boolean pageClass = Page.class.isAssignableFrom(methodParameter.getParameterType());

        return pageClass;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        Integer pageNo = Integer.valueOf(nativeRequest.getParameter("pageNo"));
        Integer pageSize = Integer.valueOf(nativeRequest.getParameter("pageSize"));
        Page page = new Page();
        page.setSize(pageSize);
        page.setCurrent(pageNo);

        log.info("pageSize:{} pageNo:{} ", pageSize, pageNo);

        return page;
    }
}