package com.tensquare.test.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-07-07 00:06:24
 * @describe:
 */
@Repository
public class UserDomeDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
}
