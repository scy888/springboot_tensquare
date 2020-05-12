package com.tensquare.article.dao;

import com.tensquare.article.pingan.CasePerson;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-05-12 20:32:01
 * @describe:
 */
@Mapper
public interface CasePersonDao {
    @Insert("insert into tb_case_person(case_person_id,case_team_id,case_person_name,total_score,effective_score,service_score,contribute_score,create_date)values" +
            "(#{casePersonId},#{caseTeamId},#{casePersonName},#{totalScore},#{effectiveScore},#{serviceScore},#{contributeScore},#{createDate})")
    void addCasePerson(CasePerson casePerson);

    @Select("select case_person_id casePersonId,case_team_id caseTeamId,case_person_name casePersonName,total_score totalScore," +
           "effective_score effectiveScore,service_score serviceScore,contribute_score contributeScore,create_date createDate from tb_case_person")
    List<CasePerson> selectCasePersonList();
}
