package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener{
    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String signName;//签名
    @RabbitHandler
    public void sendSms(Map<String,String> map){
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("手机号:"+mobile+"  "+"验证码:"+code);
         try {
            smsUtil.sendSms(mobile, templateCode, signName, "{\"code\":"+ map.get("code") +"}");
        } catch (ClientException e) {
            e.printStackTrace();
       }
    }
}
