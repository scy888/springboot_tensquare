package com.tensquare.article.dao;

import entity.GuaranteeExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-15 20:35:44
 * @describe:
 */
@Mapper
public interface GuaranteeExtDao {
    void addGuaranteeExtList(@Param("guaranteeExtList") List<GuaranteeExt> guaranteeExtList);

    List<GuaranteeExt> selectGuaranteeExtList(String guaranteeId);
}
