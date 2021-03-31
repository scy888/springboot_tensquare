package com.tensquare.batch.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.service
 * @date: 2020-07-12 00:38:22
 * @describe:
 */
@Service
@Slf4j
public class BatchService {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRegistry jobRegistry;

    public JobExecution start(Map<String, Object> map) throws Exception {
        Job job = jobRegistry.getJob((String) map.get("jobName"));
        log.info("获取的job：{}", job);
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("age")) {
                jobParametersBuilder.addLong(entry.getKey(), (Long) entry.getValue());
            } else {
                jobParametersBuilder.addString(entry.getKey(), (String) entry.getValue());
            }
        }
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        log.info("获取的job参数：{}", jobParameters);
        log.info("job:{}", job);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return jobExecution;
    }

    public String getCsv(Map<String, Object> map) throws Exception {
        Job job = jobRegistry.getJob((String) map.get("jobName"));
        log.info("获取的job：{}", job);
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue().getClass().equals(String.class)) {
                jobParametersBuilder.addString(entry.getKey(), (String) entry.getValue());
            } else {
                jobParametersBuilder.addLong(entry.getKey(), (Long) entry.getValue());
            }
        }
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(job, jobParameters);
        return jobParameters.toString();
    }

    @SneakyThrows
    public JobExecution start(String jobName, String param, String batchDate) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName", jobName)
                .addString("param", param)
                .addString("batchDate", batchDate)
                .toJobParameters();
        Job job = jobRegistry.getJob(jobName);
        log.info("job:{}", job);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return jobExecution;
    }

}
