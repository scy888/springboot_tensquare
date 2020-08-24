package com.tensquare.test.handler;

import com.tensquare.test.annotation.AdminId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.handler
 * @date: 2020-08-24 22:18:50
 * @describe:
 */

@Component(value = "handleConfig_")
@Slf4j
public class HandleConfig {
    private static final String ADMIN_ID = "ADMIN_ID";

    public HandlerMethodArgumentResolver getAdminMethodArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter methodParameter) {
                return methodParameter.getParameterType().isAssignableFrom(String.class)
                        && methodParameter.hasParameterAnnotation(AdminId.class);
            }

            @Override
            public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
                HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
                String admin_id = (String) request.getSession().getAttribute(ADMIN_ID);
                log.info("resolveArgument_admin_id:{}",admin_id);
                return admin_id;
            }
        };
    }


//    public WebMvcConfigurationSupport getWebMvcConfig() {
//        return new WebMvcConfigurationSupport() {
//            @Override
//            protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//                argumentResolvers.add(getAdminMethodArgumentResolver());
//                super.addArgumentResolvers(argumentResolvers);
//            }
//        };
//    }
}
