package com.tensquare.test.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.handler
 * @date: 2020-07-21 23:45:34
 * @describe: 自定义拦截器
 */
@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("我是拦截器....");
        return true;
    }
}
