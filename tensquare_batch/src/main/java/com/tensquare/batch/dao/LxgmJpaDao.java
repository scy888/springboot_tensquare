package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.RepaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.dao
 * @date: 2020-08-13 22:37:54
 * @describe:
 */

public interface LxgmJpaDao extends JpaRepository<RepaymentPlan,Integer> {
    @Query(value = "select count(*) from repayment_plan where batch_date=?1",nativeQuery = true)
    int countByBatchDate(LocalDate batchDate);

    @Query(value = "select * from repayment_plan where batch_date=?1 limit ?2,?3",nativeQuery = true)
    List<RepaymentPlan> selectByBatchDate(LocalDate batchDate,long index, long pageSize);
}
