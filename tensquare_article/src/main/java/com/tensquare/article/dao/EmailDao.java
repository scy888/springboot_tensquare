package com.tensquare.article.dao;

import com.tensquare.article.pojo.Email;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-01 21:33:41
 * @describe:
 */
@Mapper
public interface EmailDao {
    void addEmail(Email email);
}
