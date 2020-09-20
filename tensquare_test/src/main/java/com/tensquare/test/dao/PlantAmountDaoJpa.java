package com.tensquare.test.dao;

import com.tensquare.test.pojo.PlantAmount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-09-20 20:50:09
 * @describe:
 */
public interface PlantAmountDaoJpa extends JpaRepository<PlantAmount,Integer> {
    List<PlantAmount> findByDueBillNoIn(List<String> dueBillNo);
}
