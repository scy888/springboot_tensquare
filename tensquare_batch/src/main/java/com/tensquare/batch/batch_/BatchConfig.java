package com.tensquare.batch.batch_;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.feginClient.UserDtoFeignClient;
import com.tensquare.req.UserDtoReq;
import common.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import utils.IdWorker;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.config
 * @date: 2020-07-11 21:59:36
 * @describe:
 */
@Configuration
@Slf4j
//@StepScope
public class BatchConfig {
    @Autowired
    private UserDtoFeignClient userDtoFeignClient;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(16);
        return threadPoolTaskExecutor;
    }

    /**
     * 监听器
     */
    public JobExecutionListener getJobListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("任务执行前BatchConfig配置类内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("任务执行后BatchConfig配置类内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
            }
        };
    }

    /**
     * 第一个步骤
     */
    public Tasklet getTasklet1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                String name = (String) chunkContext.getStepContext().getJobParameters().get("name");
                List<UserDtoReq> userDtoReqList = userDtoFeignClient.select2(new UserDtoReq().setName(name));
                log.info("从步骤getTasklet1中获取userDtoReqList：{},", JSON.toJSONString(userDtoReqList));
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * 第二个步骤
     */

    public Tasklet getTasklet2() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                String name = (String) chunkContext.getStepContext().getJobParameters().get("name");
                long age = (long) chunkContext.getStepContext().getJobParameters().get("age");
                log.info("从jobParameters获取的参数name:{}，age:{}", name, age);
                //List<UserDtoReq> userDtoReqList = userDtoFeignClient.updateUserDto(name, (int) age);
                log.info("从步骤getTasklet2中获取userDtoReqList：{},", JSON.toJSONString("userDtoReqList"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * 第三个步骤
     */

    public Tasklet getTasklet3() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                log.info("执行步骤三...");
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * 第四个步骤
     */

    public Tasklet getTasklet4() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                log.info("执行步骤四...");
                return RepeatStatus.FINISHED;
            }
        };
    }

   @Bean(name = "idWorker")
    public IdWorker getIdWorker() {
        return new IdWorker(1, 1);
    }
}
