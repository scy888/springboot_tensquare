package com.tensquare.article.dao;

import com.tensquare.article.pojo.RiskBack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-18 23:29:47
 * @describe:
 */
@Mapper
public interface RiskBackDao {
    void addRiskBackList(@Param("riskBackList") List<RiskBack> riskBackList);

    List<String> getPlateNumberList(String riskBackCode);

    List<String> getDriveTelList(String riskBackCode);

    List<String> getIdCardList(String riskBackCode);

    //List<RiskBack> selectThreeParams(@Param("insuredIdCardList")List<String>insuredIdCardList,@Param("riskDriveTelList") List<String> riskDriveTelList,@Param("plateNumberList") List<String> plateNumberList);

    List<RiskBack> selectThreeParams2(Map map);

    void updateRiskBacklist(@Param("paramList") List<RiskBack> paramList);
}
