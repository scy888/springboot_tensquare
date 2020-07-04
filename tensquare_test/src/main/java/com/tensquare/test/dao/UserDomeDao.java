package com.tensquare.test.dao;

import com.tensquare.test.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao.UserDomeDao
 * @date: 2020-07-04 16:16:29
 * @describe:
 */
public interface UserDomeDao extends JpaRepository<User,Integer> {
}
