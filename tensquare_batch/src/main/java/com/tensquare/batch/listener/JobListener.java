package com.tensquare.batch.listener;

import common.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

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
//    @Autowired
//    private JavaMailSender mailSender;
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("开始执行作业{}：", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("完成执行作业{}：", jobExecution.getJobInstance().getJobName());

    }
}

