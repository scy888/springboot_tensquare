package com.tensquare.article.dao;

import com.tensquare.article.pojo.Province;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-04 23:04:13
 * @describe:
 */
@Mapper
public interface ProvinceDao {
    List<Province> findAll();

    void addProvince(Province province);

    void update(Province province);

    void delectByIds(@Param("idList") List<String> idList);

    List<Map<String, Object>> selectByNameAndCode(@Param("provinceName") String provinceName,@Param("provinceCode") String provinceCode);

    List<Province> selectAllProvince();

    void addProvince2(Map<String, Object> provinceMap);

    void deleteProvinceById(String provinceId);
}
