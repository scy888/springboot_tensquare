package com.tensquare.test.dao;

import com.tensquare.test.pojo.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao.UserDomeDao
 * @date: 2020-07-04 16:16:29
 * @describe:
 */
public interface UserDomeDao extends JpaRepository<User,Integer> {
    List<User> findByUserIdInOrderByAgeDesc(List<Integer> idList);

    List<User> findByNameLike(String name);

    List<User> findByAgeGreaterThanEqualOrderByBirthdayAsc(int age);

    List<User> findByAgeOrName(int age, String name);

   @Query(value = "update tb_user_dto set address=?2 where user_id =?1",nativeQuery = true)
   @Modifying
   @Transactional
    void updateBy(int id, String address);

   @Query(value = "select * from tb_user_dto where address=?1 or sex=?2",nativeQuery = true)
    List<User> selectBy(String address, String sex);
}
