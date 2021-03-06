package com.tensquare.test.handler;

import com.tensquare.test.annotation.AdminName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.handler
 * @date: 2020-07-21 23:18:47
 * @describe: 自定义方法参数解析器
 */
@Component
@Slf4j
public class AdminMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String ADMIN="admin";
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        return  methodParameter.getParameterType().isAssignableFrom(String.class)
                && methodParameter.hasParameterAnnotation(AdminName.class);

    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        String admin = (String) request.getSession().getAttribute(ADMIN);
//        log.info("方法参数拦截器AdminMethodArgumentResolver,admin:{}",admin);
//        String admin2 = (String) nativeWebRequest.getAttribute(ADMIN, RequestAttributes.SCOPE_REQUEST);
//        log.info("方法参数拦截器AdminMethodArgumentResolver,admin2:{}",admin2);
        String header = request.getHeader("header");
        log.info("header:{}",header);
        HttpSession session = request.getSession();
        session.setAttribute(header,"呵呵呵" );
        String value = (String) session.getAttribute(header);
        log.info("session的值:{}",value);
        return value;
    }
}
