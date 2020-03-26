package com.tensquare.article.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.article.jiekou.ProvinceServince;
import com.tensquare.article.pojo.Province;
import common.ResponeData;
import common.ResultEnum;
import common.ResultMessage;
import common.StatusCode;
import entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.controller
 * @date: 2020-02-04 23:11:32
 * @describe:
 */
@RestController
@CrossOrigin
@RequestMapping("/province")
public class ProvinceCotroller {
    private static final Logger logger= LoggerFactory.getLogger(ProvinceCotroller.class);
    @Autowired
    private ProvinceServince provinceServince;
    @Value("${scy}")
    private String scy;

    @RequestMapping("/all")
    public ResponeData<List<Province>> findAll() throws IOException {
    List<Province> provinceList=provinceServince.findAll();
    logger.info("scy{}:"+scy);
    logger.info("provinceList:"+ JSON.toJSONString(provinceList));
    return new ResponeData<List<Province>>(true, ResultEnum.QUERY_SUCCESS.getStatusCode(),ResultEnum.QUERY_SUCCESS.getMessage(),provinceList );
    }
    @RequestMapping("/add")
    public ResponeData<Void> addProvince(@RequestBody String paramJson){
        logger.info("参数paramJson{}："+paramJson);
        provinceServince.addProvince(paramJson);
        return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS );
    }
    @RequestMapping("/update")
    public ResponeData<Void> updateById(@RequestBody Province province){
        provinceServince.updateById(province);
        return new ResponeData<Void>(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS );
    }
    @RequestMapping("/delete")
    public ResponeData<Void> delectByIds(@RequestBody String paramJson){
        JSONObject jsonObject = JSON.parseObject(paramJson);
       // String ids = jsonObject.getObject("ids", String.class);
        String ids = jsonObject.getString("ids");
        //String ids = jsonObject.getJSONObject("ids").toJavaObject(String.class);报错
        List<String> idList = Arrays.asList(ids.split(","));
        //String[] ids1 = jsonObject.getJSONArray("ids").toJavaObject(String[].class);
        //String[] ids1 = jsonObject.getObject("ids", String[].class);
        provinceServince.delectByIds(idList);

        return new ResponeData<Void>(true,StatusCode.DELETESUCCESS , ResultMessage.DELETESUCCESS);

    }
    @RequestMapping("/select/{provinceName}/{provinceCode}")
    public ResponeData<List<Province>> selectByNameAndCode( @PathVariable String provinceName,@PathVariable String provinceCode){
        logger.info("provinceName{}:"+provinceName+",provinceCode{}:"+provinceCode);
       List<Map<String,Object>> mapList= provinceServince.selectByNameAndCode(provinceName,provinceCode);
        List<Province> provinceList = JSON.parseArray(JSON.toJSONString(mapList)).toJavaList(Province.class);
        return new ResponeData<List<Province>>(true,StatusCode.QUERYSUCCESS , ResultMessage.QUERYSUCCESS, provinceList);
    }
}
