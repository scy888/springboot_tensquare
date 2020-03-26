package com.tensquare.article.dao;

import entity.Guarantee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-15 20:35:26
 * @describe:
 */
@Mapper
public interface GuaranteeDao {
    void addGuaranteeList(@Param("guaranteeList") List<Guarantee> guaranteeList);

    List<Guarantee> selectGuaranteeList();

    List<Guarantee> getGuaranteeList(@Param("dateList") List<String> dateList);

    List<Guarantee> getGuaranteeList2(@Param("startDate") String startDate,@Param("endDate") String endDate);
}
