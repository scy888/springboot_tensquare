package com.tensquare.article.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-11 22:41:47
 * @describe:
 */
public class Department implements Serializable {
    /** 主键 */
    private String departmentId;
    /** 机构编码 */
    private String departmentCode;
    /** 机构名 */
    private String departmentName;
    /** 机构等级 */
    private String departmentLevel;
    /** 父类编码 */
    private String fatherCode;
    /** 子类机构 */
    private List<Department> childrenDepartmentList;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLevel() {
        return departmentLevel;
    }

    public void setDepartmentLevel(String departmentLevel) {
        this.departmentLevel = departmentLevel;
    }

    public String getFatherCode() {
        return fatherCode;
    }

    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }

    public List<Department> getChildrenDepartmentList() {
        return childrenDepartmentList;
    }

    public void setChildrenDepartmentList(List<Department> childrenDepartmentList) {
        this.childrenDepartmentList = childrenDepartmentList;
    }
}
