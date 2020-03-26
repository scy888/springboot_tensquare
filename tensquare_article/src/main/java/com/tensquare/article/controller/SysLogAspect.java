package com.tensquare.article.controller;

import com.alibaba.fastjson.JSON;
import com.tensquare.article.pojo.SysLog;
import com.tensquare.article.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @date: 2019-09-20 23:19:48
 */
@Component
@Aspect
@EnableAspectJAutoProxy
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    private static final String POINTCUT = "execution(* com.tensquare.article.controller..*.*(..))";

    @Pointcut(POINTCUT)
    public void pointCut() {

    }

    // @Around("execution(* com.tensquare.article.controller.*.*(..))")
    @Around("pointCut()")
    public Object getLog(ProceedingJoinPoint jp) throws Throwable {
        logger.info("******开始对controller层方法进行拦截********");
        SysLog sysLog = new SysLog();
        //sysLog.setId(new Random().nextInt(10));
        String username = "scy";
        //String name = SecurityContextHolder.getContext().getAuthentication().getName();
        sysLog.setUserName(username);
        String ip = request.getRemoteAddr();
        logger.info("ip{}:" + ip);
        sysLog.setIp(ip);
        String uri = request.getRequestURI();

        logger.info("uri{}:" + uri);
        sysLog.setUri(uri);
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sysLog.setVisitTime(new Date());
        Class<?> clazz = jp.getTarget().getClass();
        String className = clazz.getName();
        sysLog.setClassName(className);
        /** 方法签名 */
        MethodSignature signature = (MethodSignature) jp.getSignature();
        /** 方法名 */
        String methodName = signature.getMethod().getName();
        sysLog.setMethodName(className + "." + methodName + "()");
        /** 参数类型 */
        Class[] parameterTypes = signature.getParameterTypes();
        if (null != parameterTypes) {
            sysLog.setParamsType(Arrays.toString(parameterTypes));
        }
        /** 参数类型名 */
        String[] parameterNames = signature.getParameterNames();
        if (null != parameterNames) {
            sysLog.setParamsName(Arrays.toString(parameterNames));
        }
        /** 参数值 */
        Object[] paramsValues = jp.getArgs();
        if (null != paramsValues) {
            /** 新建一个数组,用来存放,参数名=参数值  */
            Object[] tatoal = new Object[parameterNames.length];
            for (int i = 0; i < parameterNames.length; i++) {
                tatoal[i] = parameterNames[i] + "=" + paramsValues[i];
            }
            sysLog.setParamsValue(Arrays.toString(tatoal));
        }
        Long startTime = System.currentTimeMillis();
        Object object = jp.proceed();
        Long endTime = System.currentTimeMillis();
        sysLog.setLostTime(endTime - startTime);
        if (object != null) {
            //sysLog.setReturnClassName(object.getClass().getName());
            sysLog.setReturnClassName(signature.getReturnType().getName());
            sysLog.setReturnValue(object.toString());
        } else {
            sysLog.setReturnClassName("java.lang.Object");
            sysLog.setReturnValue("void");
        }
        logger.info(JSON.toJSONString(sysLog));
        sysLogService.addSysLog(sysLog);
        //logger.info("object{}:"+JSON.toJSONString(object));
        return object;
    }
}
