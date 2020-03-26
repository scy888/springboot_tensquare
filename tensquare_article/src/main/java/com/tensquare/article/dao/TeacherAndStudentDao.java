package com.tensquare.article.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-08 18:52:30
 * @describe:
 */
@Mapper
public interface TeacherAndStudentDao {
    void addStudentIdAndTeacherId(@Param("studentId") String studentId,@Param("teacherId") String teacherId);

    List<Map<String, String>> getTeacherIdByStudentId(String studentId);

    void deleteRelation(@Param("idArray") String[] idArray);

    void deleteRelations(String studentId);
}
