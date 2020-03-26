package com.tensquare.article.dao;

import com.tensquare.article.pojo.SysLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @date: 2019-09-21 00:05:50
 */
@Mapper
public interface SysLogDao{
   @Insert("insert into tb_syslog(id,username,ip,uri,visitTime," +
           "lostTime,className,methodName,paramsType,paramsName,paramsValue,returnClassName,returnValue)" +
           " values(#{id},#{userName},#{ip},#{uri},#{visitTime}," +
           "#{lostTime},#{className},#{methodName},#{paramsType},#{paramsName},#{paramsValue},#{returnClassName},#{returnValue})")
    void addSysLog(SysLog sysLog);

   @Delete("delete from tb_syslog")
    void deleteSysLog();
}

