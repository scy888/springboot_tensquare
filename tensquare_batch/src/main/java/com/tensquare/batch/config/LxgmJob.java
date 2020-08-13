package com.tensquare.batch.config;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.dao.LxgmJpaDao;
import com.tensquare.batch.pojo.RepaymentPlan;
import com.tensquare.req.LxgmRepaymentPlanReq;
import common.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.config
 * @date: 2020-08-12 22:41:39
 * @describe:
 */
@Component
@Slf4j
public class LxgmJob {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private LxgmTasklet lxgmTasklet;
    @Autowired
    private LxgmJpaDao lxgmJpaDao;

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
    Step getStep() {
        return stepBuilderFactory.get("getStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                        PageBean<RepaymentPlan> pageBean = new PageBean<>();
                        log.info("步骤 chunkContext参数：{}", jobParameters);
                        String batchDate = (String) jobParameters.get("batchDate");
                        long pageNum = (long) jobParameters.get("pageNum");
                        long pageSize = (long) jobParameters.get("pageSize");
                        log.info("参数 args=>batchDate:{},pageNum:{},pageSize:{}", batchDate, pageNum, pageSize);
                        int totalCount = lxgmJpaDao.countByBatchDate(LocalDate.parse(batchDate));
                        long index = (pageNum - 1) * pageSize;
                        List<RepaymentPlan> data=lxgmJpaDao.selectByBatchDate(LocalDate.parse(batchDate),index,pageSize);
                        pageBean.setPageNum((int) pageNum);
                        pageBean.setPageSize((int) pageSize);
                        pageBean.setTatolCount(totalCount);
                        pageBean.setTotalPage((int) Math.ceil(totalCount*1.0/pageSize));
                        pageBean.setData(data);
                        log.info("pageBean:{}", JSON.toJSONString(pageBean));
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job getLxgmReadAndWriterJob() {
        return jobBuilderFactory.get("lxgmJob")
                .incrementer(new RunIdIncrementer())
                .listener(lxgmTasklet.getLxgmListener())
                //.start(getLxgmReadAndWriterStep())
                .start(getStep())
                .build();
    }
}
