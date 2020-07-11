package com.tensquare.batch.controller;

import com.tensquare.batch.pojo.Instance;
import com.tensquare.batch.pojo.Job;
import com.tensquare.batch.pojo.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.controller
 * @date: 2020-07-11 22:02:07
 * @describe:
 */
@Slf4j
@RestController
@RequestMapping("/batch")
public class BatchController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher jobLauncher;

    @RequestMapping("/kafka")
    public String hello(@RequestParam String worlds) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("hello", worlds);
        StringBuilder result = new StringBuilder("发送：" + worlds + "，结果：");
        future.addCallback(new ListenableFutureCallback() {

            @Override
            public void onSuccess(Object o) {
                log.error("发送消息成功：{}", o.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                result.append(throwable.getMessage());
                log.error("发送消息失败：{}", throwable.getMessage());
            }
        });
        return "发送消息成功";
    }

    @GetMapping("/jobInfo/{jobName}")
    public List<Instance> jobInfo(@PathVariable String jobName) {
        List<Instance> instanceList = new ArrayList<>();
        List<String> jobNames = jobExplorer.getJobNames();
        log.info("所有的job名称,jobNames{}:", jobNames);
        /** 一个instance对应多个job*/
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 20);
        for (JobInstance jobInstance : jobInstances) {
            Instance instance = new Instance();
            instance.setId(jobInstance.getInstanceId());
            instance.setJobName(jobName);
            log.info("job实例信息:{}:" + instance);
            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
            Collection<Job> jobCollection = new ArrayList<>();
            for (JobExecution jobExecution : jobExecutions) {
                Job job = new Job();
                job.setCreateDate(jobExecution.getCreateTime());
                job.setEndDate(jobExecution.getEndTime());
                job.setExitStatus(jobExecution.getExitStatus());
                job.setId(jobExecution.getJobId());
                job.setIsRunning(jobExecution.isRunning());
                job.setIsStopping(jobExecution.isStopping());
                job.setJobParameters(jobExecution.getJobParameters());
                job.setStartDate(jobExecution.getStartTime());
                log.info("jobExecution_信息:{}:" + job);
                /** 一个job对应多个step */
                Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
                Collection<Step> stepCollection = new ArrayList<>();
                for (StepExecution stepExecution : stepExecutions) {
                    Step step = new Step();
                    step.setCommitCount(stepExecution.getCommitCount());
                    step.setEndDate(stepExecution.getEndTime());
                    step.setExitStatus(stepExecution.getExitStatus());
                    step.setId(stepExecution.getId());
                    step.setLastDate(stepExecution.getLastUpdated());
                    step.setStepName(stepExecution.getStepName());
                    step.setStartDate(stepExecution.getStartTime());
                    step.setReadCount(stepExecution.getReadCount());
                    step.setWriteCount(stepExecution.getWriteCount());
                    step.setStatus(stepExecution.getStatus());
                    stepCollection.add(step);
                }
                job.setStepCollection(stepCollection);
                jobCollection.add(job);
            }
            instance.setJobCollection(jobCollection);
            instanceList.add(instance);
        }
        return instanceList;
    }
}
