package com.tensquare.article.dao;

import com.tensquare.article.pojo.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-13 21:33:18
 * @describe:
 */
@Mapper
public interface PolicyDao {
    void addPolicyList(@Param("policyList") List<Policy> policyList);

    List<Policy> selectPoicyList();

    void updatePolicy(Policy policy);

    void deletePolicyById(Policy policy);
}
