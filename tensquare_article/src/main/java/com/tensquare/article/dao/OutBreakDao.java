package com.tensquare.article.dao;

import com.tensquare.article.pojo.OutBreak;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-04-07 21:46:30
 * @describe:
 */
@Mapper
public interface OutBreakDao {
    void addOutBreakList(@Param("outBreakList") List<OutBreak> outBreakList);
}
