package com.tensquare.article.jiekou;

import common.ClaimpptException;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.jiekou
 * @date: 2020-03-19 20:44:51
 * @describe: http rest 请求接口
 */
public interface HttpRestOperations {
    /**
     * @Description: restful get 请求,从服务器中取资源
     * @methodName:
     * @Param: url 请求的requestMapping的地址
     * @Param: responseType 被请求的方法返回类型
     * @Param: urlVariables 请求的参数
     * @return:
     * @Author: scyang
     * @Date: 2020/3/19 20:49
     */
    public <T> T getForObject(String url,Class<T> responseType,Object...urlVariables) throws ClaimpptException;


    /**
     * @Description: restful get 请求,从服务器中取资源
     * @methodName:
     * @Param: url 请求的requestMapping的地址
     * @Param: responseType 被请求的方法返回类型
     * @Param: urlVariables 请求的参数
     * @return:
     * @Author: scyang
     * @Date: 2020/3/19 20:49
     */
    public <T> T postForObject(String url,Object requestObject,Class<T> responseType,Object...urlVariables) throws ClaimpptException;
}
