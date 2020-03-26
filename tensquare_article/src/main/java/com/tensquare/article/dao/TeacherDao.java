package com.tensquare.article.dao;

import com.tensquare.article.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-08 17:47:02
 * @describe:
 */
@Mapper
public interface TeacherDao {
    void addTeacherList(@Param("teacherList") List<Teacher> teacherList);

    void deleteTeacherById(String teacherId);

    Teacher selectTeacherById(String teacherId);
}
