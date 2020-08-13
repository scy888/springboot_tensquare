package com.tensquare.batch.config;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.controller.LxgmController;
import com.tensquare.batch.pojo.RepaymentPlan;
import com.tensquare.req.LxgmRepaymentPlanReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.config
 * @date: 2020-08-12 21:16:32
 * @describe: 乐信国民步骤
 */
@Component
@Slf4j
public class LxgmTasklet {
    @Value("${url.lxgm}")
    private String url;
   @Autowired
   private LxgmController lxgmController;

    /** 数据的写入 */
   // @StepScope
    public FlatFileItemReader<LxgmRepaymentPlanReq> getLxgmRead(/*@Value("#{jobParameters[batchDate]}")String batchDate*/){
        String dateStr = LocalDate.parse("2020-06-06").format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = url + "/" + dateStr + "/LX_GMXT_QC_05_WS_" + dateStr + ".csv";
        log.info("路径，path:{}",path);
        Resource resource = new FileSystemResource(path);
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(",");
        lineTokenizer.setNames("projectNo", "partnerNo", "dueBillNo", "term", "termStartDate", "termDueDate", "termGraceDate", "termTermPrin", "termTermInt", "termTermPenalty", "termTermFee", "termRepayPrin", "termRepayInt", "termRepayPenalty", "termRepayFee", "termReducePrin", "termReduceInt", "termReducePenalty", "termReduceFee", "termStatus", "settleDate", "effectDate");
        lineTokenizer.setStrict(false);

        DefaultLineMapper<LxgmRepaymentPlanReq> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(new CsvBeanWrapperFieldSetMapper<>(LxgmRepaymentPlanReq.class));
        return new FlatFileItemReaderBuilder<LxgmRepaymentPlanReq>()
                .resource(resource)
                .name("读取05.csv")
                .addComment("读取05.csv")
                .linesToSkip(1)
                .lineMapper(defaultLineMapper)
                .build();
    }

    /** 数据的转换 */
    public ItemProcessor<LxgmRepaymentPlanReq, RepaymentPlan> getLxgmProcessor(){
        return new ItemProcessor<LxgmRepaymentPlanReq, RepaymentPlan>(){
            @Override
            public RepaymentPlan process(LxgmRepaymentPlanReq LxgmRepaymentPlanReq) throws Exception {
                log.info("LxgmRepaymentPlanReq:{}",JSON.toJSONString(LxgmRepaymentPlanReq));
                RepaymentPlan repaymentPlan=new RepaymentPlan();
                BeanUtils.copyProperties(LxgmRepaymentPlanReq,repaymentPlan );
                log.info("repaymentPlan:{}",JSON.toJSONString(repaymentPlan));
                return repaymentPlan;
            }
        };
    }
    /** 数据的写入 */

    public ItemWriter<RepaymentPlan> getLxgmWriter(){
        return new ItemWriter<RepaymentPlan>(){
            @Override
            public void write(List<? extends RepaymentPlan> repaymentPlans) throws Exception {
                repaymentPlans=repaymentPlans.stream().peek(e-> e.setBatchDate(LocalDate.parse("2020-06-06"))).collect(Collectors.toList());
                log.info("写入转换repaymentPlans:{}", JSON.toJSONString(repaymentPlans));
                lxgmController.saveList((List<RepaymentPlan>) repaymentPlans);
            }
        };
    }
   /** 监听器 */
    public JobExecutionListener getLxgmListener(){
        return new JobExecutionListener(){
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("开始执行乐信国民作业{}：", jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("完成执行乐信国民作业{}：", jobExecution.getJobInstance().getJobName());
            }
        };
    }
    /** 线程池 */
    @Bean
    public TaskExecutor getLxmTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        return threadPoolTaskExecutor;
    }
}
