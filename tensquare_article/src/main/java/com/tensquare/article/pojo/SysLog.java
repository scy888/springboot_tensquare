package com.tensquare.article.pojo;

import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @date: 2019-09-20 22:51:18
 */
public class SysLog implements Serializable {
    private int id;

    private String userName;

    private String ip;

    private String uri;

    private Date visitTime;

    private Long lostTime;

    private String className;

    private String methodName;

    private String paramsType;

    private String paramsName;

    private String paramsValue;

    private String returnClassName;

    private String returnValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Long getLostTime() {
        return lostTime;
    }

    public void setLostTime(Long lostTime) {
        this.lostTime = lostTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamsValue() {
        return paramsValue;
    }

    public void setParamsValue(String paramsValue) {
        this.paramsValue = paramsValue;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getParamsType() {
        return paramsType;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public String getParamsName() {
        return paramsName;
    }

    public void setParamsName(String paramsName) {
        this.paramsName = paramsName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysLog.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("userName='" + userName + "'")
                .add("ip='" + ip + "'")
                .add("uri='" + uri + "'")
                .add("visitTime=" + visitTime)
                .add("lostTime=" + lostTime)
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("paramsType='" + paramsType + "'")
                .add("paramsName='" + paramsName + "'")
                .add("paramsValue='" + paramsValue + "'")
                .add("returnClassName='" + returnClassName + "'")
                .add("returnValue='" + returnValue + "'")
                .toString();
    }
}
