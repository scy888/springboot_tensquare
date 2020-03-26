package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import entity.StatusCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)//被调用微服务的名称
public interface LabelClient {//在调用微服务的一方创建一个接口
    @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)//拷贝被调用微服务的controller层里面被调用的一个方法
    public Result findById(@PathVariable("labelId") String labelId);// @PathVariable注解一定要指定参数名称,否则出错

}
