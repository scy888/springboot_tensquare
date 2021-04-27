package com.tensquare.batch.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.batch.batch_.BatchConfig;
import com.tensquare.batch.feginClient.UserDtoFeignClient;
import com.tensquare.batch.feginClient.UserFeignClient;
import com.tensquare.batch.pojo.Instance;
import com.tensquare.batch.pojo.Job;
import com.tensquare.batch.pojo.Step;
import com.tensquare.batch.service.BatchService;
import com.tensquare.client.UserClient;
import com.tensquare.req.UserDtoReq;
import common.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.LoggingSessionOutputBuffer;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import utils.IdWorker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private BatchService batchService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private UserDtoFeignClient userDtoFeignClient;
    @Autowired
    @Qualifier(value = "idWorker")
    private IdWorker getIdWorker22;

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

    @GetMapping("/start/{jobName}/{batchDate}")
    public String start(@PathVariable String jobName,
                        @PathVariable String batchDate,
                        @RequestParam(required = false) String param
    ) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("jobName", jobName);
        map.put("param", param == null ? "system" : param);
        map.put("batchDate", batchDate);
        return batchService.start(map).toString();
    }

    @GetMapping("/csv/{jobName}")
    public String getCsv(@PathVariable String jobName,
                         @RequestParam(required = false, defaultValue = "2020-06-06") String batchDate,
                         @RequestParam(required = false, defaultValue = "system") String param,
                         @RequestParam(required = false, defaultValue = "2") long pageNum,
                         @RequestParam(required = false, defaultValue = "2") long pageSize) throws Exception {
        Map<String, Object> map = new LinkedHashMap();
        map.put("jobName", jobName);
        map.put("startDate", LocalDate.now().toString());
        map.put("param", param);
        map.put("batchDate", batchDate);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        return batchService.getCsv(map);
    }

    @RequestMapping("/provider/{name}/{age}")
    public List<UserDtoReq> getUserDtoReqs(@PathVariable("name") String name, @PathVariable("age") int age) {

        List<UserDtoReq> userDtos = userClient.getUserDtos(name, age);
        log.info("userDtos:{}", JSON.toJSONString(userDtos));
        System.out.println("List<UserDtoReq> getUserDtoReqs...");
        return userDtos;
    }

    @RequestMapping("/feign")
    public List<UserDtoReq> get() {
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setName("赵敏");
        List<UserDtoReq> userDtoReqs = userDtoFeignClient.select2(userDtoReq);
        log.info("batch服务调用test服务,userDtoReqs:{}", JSON.toJSONString(userDtoReqs));
        List<UserDtoReq> userDtoReqList = userDtoFeignClient.updateUserDto(userDtoReqs.get(0).getName(), 1);
        log.info("batch服务调用test服务,userDtoReqList:{}", JSON.toJSONString(userDtoReqList));
        userDtoReqs.addAll(userDtoReqList);
        Set<UserDtoReq> set = new HashSet<>(userDtoReqs);
        return new ArrayList<UserDtoReq>(set);
    }

    @RequestMapping(value = "/text", method = RequestMethod.GET)
    public String gettext() {
        long nextId = getIdWorker22.nextId();
        return nextId + "";
    }

}
