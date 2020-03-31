package com.tensquare.article.jiekou;

import common.ClaimpptException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.jiekou
 * @date: 2020-03-31 23:57:01
 * @describe: 反射接口类,通过反射beanId获取bean对象,并执行其方法
 */
public interface ReflectServince {

    public Object getBeanByName(String beanName) throws ClaimpptException;

    public Object reflectMethodByOneParam(String beanName,String methodName,Object paramObject) throws ClaimpptException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    public Object reflectMethodByAnyParams(String beanName,String methodName,Object... paramObject) throws InvocationTargetException, ClaimpptException, IllegalAccessException, NoSuchMethodException;

}
