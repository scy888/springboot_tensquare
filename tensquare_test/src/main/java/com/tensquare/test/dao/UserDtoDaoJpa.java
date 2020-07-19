package com.tensquare.test.dao;

import com.tensquare.test.pojo.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-07-19 17:52:32
 * @describe:
 */
public interface UserDtoDaoJpa extends JpaRepository<UserDto,Integer> {

}
