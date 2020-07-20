package com.tensquare.test.dao;

import com.tensquare.test.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-07-20 22:12:54
 * @describe:
 */
public interface AdminDaoJpa extends JpaRepository<Admin,String> {
    Admin findByAdmin(String admin);
}
