package com.tensquare.batch.batch;

import com.tensquare.batch.pojo.Student;
import common.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 11:52:56
 * @describe:
 */
@Slf4j
@StepScope
@Component
public class SelectTask implements Tasklet {
    @Autowired
    private JpaRepository<Student,Integer> jpaRepository;
    @Autowired
    private JacksonUtils jacksonUtils;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<Student> studentList = jpaRepository.findAll();
        log.info("studentList:{}",jacksonUtils.toString(studentList));
        return RepeatStatus.FINISHED;
    }
}
