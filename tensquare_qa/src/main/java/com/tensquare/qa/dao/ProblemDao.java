package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    @Query("select p from Problem p where id in( select problemid from Pl where labelid=?1 ) order by replytime desc")
  public Page<Problem> findBylabelId(String label,Pageable of);

    @Query("select p from Problem p where id in( select problemid from Pl where labelid=?1 ) order by reply desc")
    Page<Problem> findByhotlistlabelId(String label, Pageable of);

    @Query("select p from Problem p where id in( select problemid from Pl where labelid=?1 ) and reply=0 order by createtime desc")
    Page<Problem> findBywaitlistlabelId(String label, Pageable of);

}
