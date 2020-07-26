package com.tensquare.test.dao;

import com.tensquare.test.pojo.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-07-19 17:52:32
 * @describe:
 */
public interface UserDtoDaoJpa extends JpaRepository<UserDto,Integer> {
    @Query(value = "update user_dto set sex= ?2,context= ?1 where age= ?4 and name= ?3",nativeQuery = true)
    @Modifying
    @Transactional
    void updateByNameAndAge(String context, String sex, String name, Integer age);

//    @Query(value = "update user_dto set context= :#{#userDto.context},sex= :#{#userDto.sex} where name= :#{#userDto.name} and age= :#{#userDto.age}", nativeQuery = true)
//    @Modifying
//    @Transactional
//    void updateByNameAndSex(UserDto userDto);

}
