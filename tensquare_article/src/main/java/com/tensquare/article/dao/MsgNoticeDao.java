package com.tensquare.article.dao;

import com.tensquare.article.pingan.MsgNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-04-25 11:27:13
 * @describe:
 */
@Mapper
public interface MsgNoticeDao {
    void createNotice(MsgNotice msgNotice);

    void updateNotice(@Param("idsList") List<String> idsList,@Param("status") String status);
}
