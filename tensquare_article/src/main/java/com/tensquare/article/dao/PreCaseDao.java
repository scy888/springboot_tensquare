package com.tensquare.article.dao;

import com.tensquare.article.pojo.PreCase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-29 20:59:51
 * @describe:
 */
@Mapper
public interface PreCaseDao {
    @Insert("insert into tb_pre_case(pre_case_id,money,is_on_line,down_line_schedul,accident_level,death_people,hurt_people,create_date)values" +
            "(#{preCaseId},#{money},#{isOnLine},#{downLineSchedul},#{accidentLevel},#{deathPeople},#{hurtPeople},#{createDate})")
    void addCase(PreCase preCase);
}
