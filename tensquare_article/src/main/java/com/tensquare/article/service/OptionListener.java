
package com.tensquare.article.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tensquare.article.dao.OptionDao;
import com.tensquare.article.dao.PolicyDao;
import com.tensquare.article.pojo.Option;
import com.tensquare.article.pojo.Policy;
import common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-02-09 01:59:55
 * @describe:
 */

@Component
public class OptionListener {
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private PolicyDao policyDao;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OptionListener.class);

    /*@Value("${option_desc_one}")
    private String option_desc_one;
    @Value("${option_desc_two}")
    private String option_desc_two;
    @Value("${option_desc_one1}")
    private String option_desc_one1;
    @Value("${option_desc_two1}")
    private String option_desc_two1;*/
    @JmsListener(destination = "option")
    public void getSms(String msg) {
        List<Option> optionList = JSON.parseArray(msg).toJavaList(Option.class);
        for (Option option : optionList) {
            logger.info("option{ActiveMq}:" + JSON.toJSONString(option));
        }
    }

    @JmsListener(destination = "option_status")
    public void getOptionList(String textMsg) throws Exception {
        List<Option> optionList = JSON.parseArray(textMsg).toJavaList(Option.class);
        optionList.stream().findFirst().get().setOptionDesc("我偷偷修改了....");
        for (Option option : optionList) {
            logger.info("option{ActiveMq}:" + JSON.toJSONString(option));
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               Option option1 = optionList.get(0);
               String option_desc_one = "{\"人民公社\":\"应城市2\",\"李白故居\":\"安陆市2\"}";
               option1.setOptionDesc(option_desc_one);
               Option option2 = optionList.get(1);
               String option_desc_two = "[{\"羊台山\":\"龙岗区\"},{\"红树林\":\"福田区\"}]";
               option2.setOptionDesc(option_desc_two);
               JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(optionList));
               String rngs = jsonArray.getJSONObject(0).getJSONObject("optionDesc").getObject("人民公社", String.class);
               logger.info("rngs{人民公社}:" + rngs);
               String lbgj = jsonArray.getJSONObject(0).getJSONObject("optionDesc").getString("李白故居");
               logger.info("lbgj{李白故居}:" + lbgj);
               String yts = jsonArray.getJSONObject(1).getJSONArray("optionDesc").getJSONObject(0).getString("羊台山");
               logger.info("yts{羊台山}:" + yts);
               String hsl = jsonArray.getJSONObject(1).getJSONArray("optionDesc").getJSONObject(1).getString("红树林");
               logger.info("hsl{红树林}:" + hsl);
               optionDao.updateOptionList(optionList);
           }
       }).start();
    }
    @JmsListener(destination = "code")
    public void getCodeMap(Map<String,String> map){
        Map object = JSON.parseObject(map.get("map")).toJavaObject(Map.class);
        logger.info("map{ActiveMQ}:"+map.get("map"));
        String code = object.get("code").toString();
        String iphone = object.get("iphone").toString();
        String num = (String) redisTemplate.opsForValue().get(iphone);
        if (!StringUtils.isEmpyStr(num)){
            throw new ClassCastException("已经生成验证码,请在redis中查看...");
        }
        redisTemplate.opsForValue().set(iphone,code , 10, TimeUnit.MINUTES );
    }
}


