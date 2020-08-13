package com.tensquare.batch.config;

import com.tensquare.batch.pojo.RepaymentPlan;
import com.tensquare.req.LxgmRepaymentPlanReq;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.config
 * @date: 2020-08-12 22:41:39
 * @describe:
 */
@Component
public class LxgmJob {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private LxgmTasklet lxgmTasklet;

    @Bean
    public Step getLxgmReadAndWriterStep() {
        Step step = stepBuilderFactory.get("解析国民乐信CSV文件")
                .<LxgmRepaymentPlanReq, RepaymentPlan>chunk(50)
                .reader(lxgmTasklet.getLxgmRead())
                .processor(lxgmTasklet.getLxgmProcessor())
                .writer(lxgmTasklet.getLxgmWriter())
                .taskExecutor(lxgmTasklet.getLxmTaskExecutor())
                .throttleLimit(2)
                .build();
        return step;
    }
    @Bean
    public Job getLxgmReadAndWriterJob(){
        return jobBuilderFactory.get("lxgmJob")
                .incrementer(new RunIdIncrementer())
                .listener(lxgmTasklet.getLxgmListener())
                .start(getLxgmReadAndWriterStep())
                .build();
    }
}
