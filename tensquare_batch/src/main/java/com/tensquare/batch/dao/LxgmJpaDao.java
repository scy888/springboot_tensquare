package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.RepaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select due_bill_no from repayment_plan where batch_date= :batchDate limit :index,:pageSize",nativeQuery = true)
    List<String> findByBatchDate(@Param("batchDate") LocalDate batchDate, @Param("index")int index, @Param("pageSize")int pageSize);

    @Query(value = "select createdDate from #{#entityName} where dueBillNo in :dueBillNoList")
    List<LocalDate> findByDueBillNoList(@Param("dueBillNoList") List<String> dueBillNoList);
}
