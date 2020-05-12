package com.tensquare.article.dao;

import com.tensquare.article.pingan.CaseTeam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-05-12 20:31:39
 * @describe:
 */
@Mapper
public interface CaseTeamDao {
    @Insert("insert into tb_case_team(case_team_id,case_team_name,case_team_status,case_team_value)values" +
            "(#{caseTeamId},#{caseTeamName},#{caseTeamStatus},#{caseTeamValue})"
            )
    void addCaseTeam(CaseTeam caseTeam);
    @Select("select case_team_name from tb_case_team where case_team_id=#{caseTeamId}")
    String getTeamNameById(String caseTeamId);
}
