package com.tensquare.article.dao;

import com.tensquare.article.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-08 17:47:38
 * @describe:
 */
@Mapper
public interface StudentDao {
    void addStudentList(@Param("studentList") List<Student> studentList);

    void addStudentIdAndTeacherId(@Param("studentId") String studentId,@Param("teacherId") String teacherId);

    void deleteStudentByIds(@Param("idArray") String[] idArray);

    List<Student> selectAllStudent();

    List<Student> selectStudent();

    Map<String,Object> getStudentIdByStudentName(String studentName);

    void updateStudentListById(@Param("studentList") List<Student> studentList);
}
