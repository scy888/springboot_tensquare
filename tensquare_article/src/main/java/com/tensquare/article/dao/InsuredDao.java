package com.tensquare.article.dao;

import com.tensquare.article.pojo.Insured;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-16 20:42:52
 * @describe:
 */
@Mapper
public interface InsuredDao {
    void addInsured(Map<String, Object> paramMap);


    List<Insured> selectInsuredList();
}
