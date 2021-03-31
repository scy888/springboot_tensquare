package com.tensquare.batch.batch_;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.feginClient.UserDtoFeignClient;
import com.tensquare.batch.listener.JobListener;
import com.tensquare.req.UserDtoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 11:37:37
 * @describe:
 */
@Configuration
@Slf4j
public class BatchJobConfig {
    @Autowired
    private UserDtoFeignClient userDtoFeignClient;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BatchConfig batchConfig;
    @Autowired
    private JobListener jobListener;


    /******************************************************************************/
    @Bean
    public BatchConfig getBatchConfig() {
        return new BatchConfig();
    }

    /**
     * job监听器
     */

    public JobExecutionListener getJobListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("任务执行前BatchJobConfig内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("任务执行后BatchJobConfig内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
            }
        };
    }

    /**
     * 第一个步骤
     */
    @Bean
    @StepScope
    public Tasklet getTasklet1(@Value("#{jobParameters[name]}") String name) {
        log.info("从jobParameters获取的参数name:{}", name);
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
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
                // List<UserDtoReq> userDtoReqList = userDtoFeignClient.updateUserDto(name, (int) age);
                log.info("从步骤getTasklet2中获取userDtoReqList：{},");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step getStep1(Tasklet getTasklet1) {
        return stepBuilderFactory.get("step1")
                //.tasklet(getTasklet1)
                //.tasklet(batchConfig.getTasklet1())
                .tasklet(getBatchConfig().getTasklet1())
                .build();
    }

    public Step getStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet(getTasklet2())
                //.tasklet(batchConfig.getTasklet2())
                //.tasklet(getBatchConfig().getTasklet2())
                .build();
    }

    @Bean
    public Step getStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet(getBatchConfig().getTasklet3())
                .build();
    }

    @Bean
    public Step getStep4() {
        return stepBuilderFactory.get("step4")
                .tasklet(getBatchConfig().getTasklet4())
                .build();
    }

    @Bean
    public Flow getStep3AndGetStep4() {
        return new FlowBuilder<SimpleFlow>("step3Andstep4")
                .split(batchConfig.batchTaskExecutor())
                .add(new FlowBuilder<SimpleFlow>("step3")
                                .start(getStep3())
                                .build(),
                        new FlowBuilder<SimpleFlow>("step4")
                                .start(getStep4())
                                .build())
                .build();
    }

    @Bean
    public Job getJob(Step getStep1,TaskExecutor batchTaskExecutor) {
        return jobBuilderFactory.get("userJob")
                .listener(getJobListener())
                //.listener(batchConfig.getJobListener())
                //.listener(getBatchConfig().getJobListener())
                .incrementer(new RunIdIncrementer())
                .flow(getStep1)
                .next(getStep2())
                .next(getStep3AndGetStep4())
                //.next(getStep3())
                // .next(getStep4())
                .end()
                .build();
    }
}
