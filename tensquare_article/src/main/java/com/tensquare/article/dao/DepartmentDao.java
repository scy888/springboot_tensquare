package com.tensquare.article.dao;

import com.tensquare.article.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-11 23:18:21
 * @describe:
 */
@Mapper
public interface DepartmentDao {
    void addDepartmentList(@Param("departmentArray") Department[] departmentArray);

    List<Department> getDepartmentList();

    List<Department> getDepartmentListByCode(String departmentCode);

    List<Department> getDepartmentListByFatherCode(String departmentCode);
}
