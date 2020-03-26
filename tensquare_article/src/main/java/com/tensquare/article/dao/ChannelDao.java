package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Channel;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ChannelDao extends JpaRepository<Channel,String>,JpaSpecificationExecutor<Channel>{
   @Query(value = "select * from tb_channel where id=?",nativeQuery = true)
    Channel findByMap(String id);

   List<Channel> findTop9ByIsHotOrderByCreateDateDesc(String isHost);

    @Query(value = "select * from tb_channel where name=?1 and state=?2",nativeQuery = true)
    List<Channel> selectByNameAndState(String name, String state);

    List<Channel> findByNameAndState(String name, String state);

    List<Channel> findByCreateDateAndIsHot(Date createDate, String isHot);

    List<Channel> findByName(String name);
}
