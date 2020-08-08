//package com.tensquare.batch.config;
//
//import com.alibaba.fastjson.JSON;
//import com.tensquare.batch.feginClient.LxgmCsvFeignClient;
//import com.tensquare.req.LxgmRepaymentPlanReq;
//import com.tensquare.result.Result;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.Collectors;
//
///**
// * @author: scyang
// * @program: tensquare_parent
// * @package: com.tensquare.batch.config
// * @date: 2020-08-06 00:21:23
// * @describe:
// */
//@Component
//@Slf4j
//public class BatchCsvConfig {
//    @Autowired
//    private LxgmCsvFeignClient lxgmCsvFeignClient;
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//    @Value("${url.lxgm}")
//    private String url;
//
//    /**
//     * 监听器
//     */
//    public JobExecutionListener getJobListener() {
//        return new JobExecutionListener() {
//            @Override
//            public void beforeJob(JobExecution jobExecution) {
//                log.info("任务执行前BatchConfig配置类内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
//            }
//
//            @Override
//            public void afterJob(JobExecution jobExecution) {
//                log.info("任务执行后BatchConfig配置类内部注入监听器,jobInsrence:{}", jobExecution.getJobInstance());
//            }
//        };
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<LxgmRepaymentPlanReq> lxgmCsv05Reader(@Value("#{jobParameters[batchDate]}") String batchDate) {
//        String dateStr = LocalDate.parse(batchDate).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String path = url + "/" + dateStr + "/LX_GMXT_QC_05_WS_" + dateStr + ".txt";
//        log.info("路径，path:{}",path);
//        //Resource resource = new FileSystemResource("E:\\lxgm\\20200606"+"/"+"/LX_GMXT_QC_05_WS_" + dateStr + ".csv");
//        Resource resource = new FileSystemResource(path);
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(",");
//        lineTokenizer.setNames("projectNo", "partnerNo", "dueBillNo", "term", "termStartDate", "termDueDate", "termGraceDate", "termTermPrin", "termTermInt", "termTermPenalty", "termTermFee", "termRepayPrin", "termRepayInt", "termRepayPenalty", "termRepayFee", "termReducePrin", "termReduceInt", "termReducePenalty", "termReduceFee", "termStatus", "settleDate", "effectDate");
//        lineTokenizer.setStrict(false);
//
//        DefaultLineMapper<LxgmRepaymentPlanReq> defaultLineMapper = new DefaultLineMapper<>();
//        defaultLineMapper.setLineTokenizer(lineTokenizer);
//        defaultLineMapper.setFieldSetMapper(new CsvBeanWrapperFieldSetMapper<>(LxgmRepaymentPlanReq.class));
//        return new FlatFileItemReaderBuilder<LxgmRepaymentPlanReq>()
//                .resource(resource)
//                .name("读取05.csv")
//                .addComment("读取05.csv")
//                .linesToSkip(1)
//                .lineMapper(defaultLineMapper)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<LxgmRepaymentPlanReq> lxgmCsv05Writer(@Value("#{jobParameters[batchDate]}") String batchDate) {
//        log.info("lxgmCsv05Writer batchDate = {},callType = {}", batchDate);
//        return new ItemWriter<LxgmRepaymentPlanReq>() {
//            @Override
//            public void write(List<? extends LxgmRepaymentPlanReq> items) throws Exception {
//                List<LxgmRepaymentPlanReq> list = items.stream().peek(e -> e.setBatchDate(LocalDate.parse(batchDate))).collect(Collectors.toList());
//                log.info("05.csv 数据量：size:{},list:{}", list.size(), JSON.toJSONString(list));
//
//                CompletableFuture<Result> completableFuture1 = CompletableFuture.supplyAsync(() -> {
//                    log.info("开始acc-batch 调 acc-lxgm-adapter, 保存还款计划至乐信接口表");
//                    return lxgmCsvFeignClient.saveRepaymentPlan(list);
//                }).exceptionally(ex -> {
//                    throw new RuntimeException("acc-batch 调 acc-lxgm-adapter, saveRepaymentPlan 保存还款计划至乐信接口表异常：" + ex.getMessage());
//                });
//                Result result1 = completableFuture1.get();
//                log.info("lxgmCsv05Writer 执行完成，result1：{}", result1);
//            }
//        };
//    }
//
//    @Bean
//    public Step lxgmCsv05Step(FlatFileItemReader<LxgmRepaymentPlanReq> lxgmCsv05Reader,
//                              ItemWriter<LxgmRepaymentPlanReq> lxgmCsv05Writer,
//                              TaskExecutor batchTaskExecutor) {
//        Step step = stepBuilderFactory.get("解析05CSV")
//                .<LxgmRepaymentPlanReq, LxgmRepaymentPlanReq>chunk(10)
//                .reader(lxgmCsv05Reader)
//                .writer(lxgmCsv05Writer)
//                .taskExecutor(batchTaskExecutor)
//                .throttleLimit(8)
//                .build();
//        return step;
//    }
//
//    @Bean
//    public Job lxgmCsv05SJob(Step lxgmCsv05Step) {
//        return jobBuilderFactory.get("csvJob")
//                .incrementer(new RunIdIncrementer())
//                .listener(getJobListener())
//                .flow(lxgmCsv05Step)
//                .end()
//                .build();
//    }
//}
