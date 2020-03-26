package com.tensquare.article.dao;

import com.tensquare.article.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-04 21:52:05
 * @describe:
 */
@Mapper
public interface UserDao {
    void addUserArray(@Param("users") User[] users);

    User login(String userName);

    List<User> selectUserList();

    void registerUser(User user);
}
