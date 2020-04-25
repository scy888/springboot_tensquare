package com.tensquare.article.dao;

import com.tensquare.article.pingan.Employer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-04-25 16:21:17
 * @describe:
 */
@Mapper
public interface EmploverDao {
    void addEmplover(Employer employer);

    List<Employer> selectEmplover();
}
