package com.tensquare.article.dao;

import com.tensquare.article.pojo.Salvage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-23 01:44:21
 * @describe:
 */
@Mapper
public interface SalvageDao {
    void addSalvageList(@Param("salvageList") List<Map> mapList);

    List<Salvage> getSalvagePageInfo(@Param("statusCode") String statusCode);
}
