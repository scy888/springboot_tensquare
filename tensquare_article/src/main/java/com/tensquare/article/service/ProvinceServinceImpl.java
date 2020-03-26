package com.tensquare.article.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.article.dao.CityDao;
import com.tensquare.article.dao.ProvinceDao;
import com.tensquare.article.jiekou.ProvinceServince;
import com.tensquare.article.pojo.City;
import com.tensquare.article.pojo.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-02-04 23:16:49
 * @describe:
 */
@Service
public class ProvinceServinceImpl implements ProvinceServince {
    private static final Logger logger= LoggerFactory.getLogger(ProvinceServinceImpl.class);
    @Autowired
    private ProvinceDao provinceDao;
    @Autowired
    CityDao cityDao;
    @Autowired
    private IdWorker idWorker;
    @Override
    public List<Province> findAll() {
        List<Province> provinceList=null;
        try {
             provinceList = provinceDao.findAll();
            for (Province province : provinceList) {
                String provinceId = province.getProvinceId();
                List<City> cityList=cityDao.selectById(provinceId);
                cityList=cityList.stream().filter(city->city.getProvinceId().equals(provinceId)).collect(Collectors.toList());
                province.setCityList(cityList);
            }
            return provinceList;
        } catch (Exception e) {
            logger.info("异常消息:{}"+e.getMessage());
            return provinceList;
        }
    }

    @Override
    public void addProvince(String paramJson) {
        JSONObject jsonObject = JSON.parseObject(paramJson);
        Province province = jsonObject.toJavaObject(Province.class);
        province.setProvinceId(idWorker.nextId()+"");
        provinceDao.addProvince(province);
        List<City> cityList = jsonObject.getJSONArray("cityList").toJavaList(City.class);
       String provinceId = jsonObject.getObject("provinceId", String.class);
        String provinceId1 = jsonObject.getString("provinceId");
        //String provinceId2 = jsonObject.getJSONObject("provinceId").toJavaObject(String.class);报错
        // List<City> cityList = province.getCityList();
        logger.info("provinceId{}:"+provinceId);
        logger.info("provinceId1{}:"+provinceId1);
        logger.info("provinceId{}:"+province.getProvinceId());
        for (City city : cityList) {
            city.setProvinceId(province.getProvinceId());
            city.setCityId(idWorker.nextId()+"");
            cityDao.addCity(city);
        }
    }

    @Override
    public void updateById(Province province) {
        logger.info("province{}:"+JSON.toJSONString(province));
        provinceDao.update(province);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(province));
        String provinceId = jsonObject.getString("provinceId");
       cityDao.deletelList(provinceId);
        List<City> cityList = jsonObject.getJSONArray("cityList").toJavaList(City.class);
        setCityIdAndRelationId(cityList,provinceId);
        cityDao.saveCityList(cityList);

    }

    @Override
    public void delectByIds(List<String> idList) {
        provinceDao.delectByIds(idList);
        cityDao.delectByIds(idList);
    }

    @Override
    public List<Map<String, Object>> selectByNameAndCode(String provinceName, String provinceCode) {
        List<Map<String, Object>> mapList=provinceDao.selectByNameAndCode(provinceName,provinceCode);
        logger.info("mapList:{}"+JSON.toJSONString(mapList));//对应的是数据库字段
        //List<Province> provinceList = JSON.parseObject(JSON.toJSONString(mapList),List.class);//对应的是数据库字段
        List<Province> provinceList = JSON.parseArray(JSON.toJSONString(mapList)).toJavaList( Province.class);//对应的是属性字段
        logger.info("provinceList:{}"+JSON.toJSONString(provinceList));

        for (Map<String, Object> map : mapList) {
            logger.info("map{}:"+JSON.toJSONString(map));
            String provinceId = String.valueOf(map.get("province_id"));
            List<City> cityList = cityDao.selectById(provinceId);
            logger.info("cityList{}:"+JSON.toJSONString(cityList));
            map.put("cityList",cityList );
        }
        return mapList;
    }

    private void setCityIdAndRelationId(List<City> cityList,String provinceId) {
        for (City city : cityList) {
            city.setCityId(idWorker.nextId()+"");
            city.setProvinceId(provinceId);
            //cityDao.addCity(city);
        }
    }
}
