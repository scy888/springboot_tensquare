package com.tensquare.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.listener
 * @date: 2020-07-12 11:07:57
 * @describe: job监听器
 */
@Slf4j
@Component
public class JobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("开始执行作业{}：",jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("执行作业完成{}：",jobExecution.getJobInstance().getJobName());
    }
}
