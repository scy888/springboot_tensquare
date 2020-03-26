package com.tensquare.article.dao;

import com.tensquare.article.pojo.Consultation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-01 21:33:17
 * @describe:
 */
@Mapper
public interface ConsultationDao {
    void addConsultationList(@Param("consultationList") List<Consultation> consultationList);

    List<Consultation> selectConsultation();
}
