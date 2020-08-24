package com.tensquare.test.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.handler
 * @date: 2020-07-21 23:37:49
 * @describe:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private AdminMethodArgumentResolver adminMethodArgumentResolver;
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private HandleConfig handleConfig;

    @Bean
    public HandleConfig handleConfig() {
        return new HandleConfig();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**", "/**/updateBy/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(adminMethodArgumentResolver);
        //argumentResolvers.add(handleConfig.getAdminMethodArgumentResolver());
        argumentResolvers.add(handleConfig().getAdminMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }
}
