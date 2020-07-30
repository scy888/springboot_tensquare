package com.tensquare.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面类
 *
 * @author jiancai.zhou
 * @date 2020-07-10 11:25
 **/
@Slf4j
@Order(1)
@Aspect
@Component
public class LogAspect {
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Pointcut("execution(public * com.weshare.acc..*Provider*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        START_TIME.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 记录下请求内容
            log.info("req_url={}", request.getRequestURL().toString());
            log.info("req_http_method={}", request.getMethod());
            log.info("req_ip={}", request.getRemoteAddr());
        }
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("req_class_method={}.{}", declaringTypeName, methodName);
        log.info("req_args={}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("response={}", ret);
        // 处理请求耗时
        Long startTime = START_TIME.get();
        if (startTime != null) {
            log.info("req_rt={}ms", (System.currentTimeMillis() - START_TIME.get()));
            // 移除，防止线程池中使用导致线程不安全
            START_TIME.remove();
        }
    }
}
