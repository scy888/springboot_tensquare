package com.tensquare.test.dao;

import com.tensquare.test.pojo.RealAmount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-09-20 20:51:03
 * @describe:
 */
public interface RealAmountDaoJpa extends JpaRepository<RealAmount,Integer> {
    RealAmount findByDueBillNoAndTerm(String dueBillNo,int term);
}
