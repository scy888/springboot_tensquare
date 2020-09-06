package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.BatchJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.dao
 * @date: 2020-09-06 21:41:11
 * @describe:
 */
public interface BatchJobStatusJpa extends JpaRepository<BatchJobStatus,String> {

}
