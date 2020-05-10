package com.tensquare.article.dao;

import com.tensquare.article.pingan.Duty;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-05-10 20:17:12
 * @describe:
 */
@Mapper
public interface DutyDao {
    void addDuty(Duty duty);
}
