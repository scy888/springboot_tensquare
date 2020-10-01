package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.dao
 * @date: 2020-09-17 23:31:03
 * @describe:
 */
public interface StudentJpaDao extends JpaRepository<Student,Integer> {

    List<Student> findByAge(int age);
}
