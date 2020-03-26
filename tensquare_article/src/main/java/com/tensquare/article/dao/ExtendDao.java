package com.tensquare.article.dao;

import com.tensquare.article.pojo.ExtendInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-18 23:29:28
 * @describe:
 */
@Mapper
public interface ExtendDao {
    List<ExtendInfo> selectAll();
}
