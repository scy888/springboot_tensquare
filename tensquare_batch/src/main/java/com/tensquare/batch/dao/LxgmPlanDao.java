package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.RepaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.dao
 * @date: 2020-08-15 15:03:23
 * @describe:
 */
@Mapper
public interface LxgmPlanDao {

    void updateList(@Param("updateList") List<RepaymentPlan> updateList);

    List<RepaymentPlan> findDueBillNosAndTerms(@Param("repaymentPlans") List<RepaymentPlan> repaymentPlans);
}
