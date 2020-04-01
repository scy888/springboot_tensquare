package com.tensquare.article.service;

import com.tensquare.article.jiekou.ReflectServince;
import common.ClaimpptException;
import common.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-04-01 00:03:15
 * @describe:
 */
@Service
public class ReflectServinceImpl extends WebApplicationObjectSupport implements ReflectServince {
    private static ApplicationContext applicationContext;
    private final ApplicationContext getApplicationContextBySuper(){
        /**
         * @Description: 获取applicationContext容器
         * @methodName: getApplicationContextBySuper
         * @Param: []
         * @return: org.springframework.context.ApplicationContext
         * @Author: scyang
         * @Date: 2020/4/1 0:07
         */
        if (applicationContext==null){
            applicationContext=this.getApplicationContext();
        }
        return applicationContext;
    }

    @Override
    public Object getBeanByName(String beanName)  {

        return this.getApplicationContextBySuper();
    }

    @Override
    public Object reflectMethodByOneParam(String beanName, String methodName, Object paramObject) throws
            ClaimpptException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        if (StringUtils.isEmpyStr(beanName)||StringUtils.isEmpyStr(methodName)){
            throw new ClaimpptException("传入的反射beanName和methodName不能为空!!!");
        }
        try {
            Object object = this.getBeanByName(beanName);
            Class<?> clazz = object.getClass();
            Method method = clazz.getDeclaredMethod(methodName, paramObject.getClass());
            return  method.invoke(object,paramObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new ClaimpptException("反射失败："+e.getMessage());
        }
    }

    @Override
    public Object reflectMethodByAnyParams(String beanName, String methodName, Object... paramObject)
            throws InvocationTargetException,
            ClaimpptException, IllegalAccessException,
            NoSuchMethodException {
        /**
         * @Description:  反射调用
         * @methodName: reflectMethodByAnyParams
         * @Param: [beanName, methodName, paramObject]
         * @return: java.lang.Object
         * @Author: scyang
         * @Date: 2020/4/1 20:07
         */

        /** 通过beanName获取对象 */
        Object object = this.getBeanByName(beanName);
        /** 获取字节码对象 */
        Class<?> clazz = object.getClass();
        Method method=null;
        Object tagert=null;
        /** 字节码集合 */
        List<Class> classList=new ArrayList<>();
        /** 参数列表集合 */
        List<Object> paramList=new ArrayList<>();
        try {
            if (paramObject!=null && paramObject.length>0){
                for (Object param : paramObject) {
                    /** 添加参数类型字节码 */
                    classList.add(param.getClass());
                    /** 添加方法参数列表 */
                    paramList.add(param);
                }
                /** 反射调用 */
                method = clazz.getDeclaredMethod(methodName, classList.toArray(new Class[classList.size()]));
                tagert= method.invoke(object,paramList.toArray(new Object[paramList.size()]));
            }
            else {
                method = clazz.getDeclaredMethod(methodName,new Class[]{});
                tagert= method.invoke(object,new Object[]{});
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new ClaimpptException("反射调用失败 "+e.getMessage());
        }
        return tagert;
    }
}
