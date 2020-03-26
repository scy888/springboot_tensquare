package com.tensquare.article.dao;

import com.tensquare.article.pojo.Spection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-06 14:27:26
 * @describe:
 */
@Mapper
public interface SpectionDao {
    void addSpection(Spection spection);

    void deleteSpectionByIds(@Param("idList") List<String> idList);

    void updateSpection(Map<String, Object> map);

    List<Map<String, Object>> selectAllSpection();

    List<Spection> findAllSpection();

    void delteSpectionByIds(@Param("ids") String[] ids);

    void deleteSpectionbyId(String spetionId);

    List<Spection> getOptionList(Date spectionDate);
}
