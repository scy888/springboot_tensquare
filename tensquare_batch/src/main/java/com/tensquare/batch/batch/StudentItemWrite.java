package com.tensquare.batch.batch;

import com.tensquare.batch.pojo.Student;
import common.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 11:18:10
 * @describe:
 */
@Slf4j
@StepScope
@Component
public class StudentItemWrite implements ItemWriter<Student> {
    @Autowired
    private JpaRepository<Student,Integer> jpaRepository;
    @Autowired
    private JacksonUtils jacksonUtils;
    @Override
    public void write(List<? extends Student> list) throws Exception {
      log.info("开始StudentItemWrite{}：",jacksonUtils.toString(list));
        jpaRepository.saveAll(list);
    }
}
