package com.tensquare.article.dao;

import com.tensquare.article.pojo.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-04 23:04:39
 * @describe:
 */
@Mapper
public interface CityDao {
    void addCity(City city);

    List<City> selectById(String provinceId);

    void deletelList(String provinceId);

    void saveCityList(@Param("cityList") List<City> cityList);


    void delectByIds(@Param("idList") List<String> idList);

    void addCityList(@Param("cityList") List<Map> mapList);

    Map<String, Object> getProvinceIdByCityId(String cityId);

    void delectByProvinceId(@Param("idList") List<String> asList);

    void updateCityList(@Param("cityList") List<City> cityList);

    Map<String, Object> getCityIdAndProvinceIdByCityName(String cityName);
}
