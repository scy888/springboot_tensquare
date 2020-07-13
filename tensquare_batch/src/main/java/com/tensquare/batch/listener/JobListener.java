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
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JobExplorer jobExplorer;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("开始执行作业{}：", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("执行作业完成{}：", jobExecution.getJobInstance().getJobName());
        boolean flag = jobExecution.getStatus() == BatchStatus.FAILED
                || jobExecution.getExitStatus() == ExitStatus.UNKNOWN;
        // 对运行异常的job发送邮件提醒
        if (flag) {
            log.info("Job异常邮件发送开始=======>");
            Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
            List<Long> list = new ArrayList<>();
            for (StepExecution stepExecution : stepExecutions) {
                list.add(stepExecution.getId());
            }
            Long maxStepId = Collections.max(list);
            Long jobId = jobExecution.getId();
            StepExecution lastStepExecution = jobExplorer.getStepExecution(jobId, maxStepId);

            String templateContent = FreemarkerUtil.getTemplateContent("/ftl/batch-job-failure.html");
            Map<String, Object> root = new HashMap<>();
            String operator = jobExecution.getJobParameters().getString("operator", "System");
            root.put("operator", operator);
            root.put("subject", "标题");
            root.put("subject", "Job异常信息明细");
            root.put("jobName", jobExecution.getJobInstance().getJobName());
            root.put("jobStartTime", jobExecution.getStartTime());
            root.put("jobEndTime", jobExecution.getEndTime());
            root.put("jobStatus", jobExecution.getStatus());
            root.put("jobExitCode", jobExecution.getExitStatus().getExitCode());
            root.put("jobExitDescription", jobExecution.getExitStatus().getExitDescription());
            root.put("jobIsRuning", jobExecution.getExitStatus().isRunning());
            root.put("joblastUpdateTime", jobExecution.getLastUpdated());
            // root.put("jobDueTime", DateUtils.dueTimeByMin(jobExecution.getStartTime(), jobExecution.getEndTime()));
            root.put("stepName", lastStepExecution.getStepName());
            root.put("stepStartTime", lastStepExecution.getStartTime());
            root.put("stepEndTime", lastStepExecution.getEndTime());
            root.put("stepStatus", lastStepExecution.getStatus());
            root.put("stepCommitCount", lastStepExecution.getCommitCount());
            root.put("stepReadCount", lastStepExecution.getReadCount());
            root.put("stepWriteCount", lastStepExecution.getWriteCount());
            root.put("stepExitCode", lastStepExecution.getExitStatus().getExitCode());
            root.put("stepExitDescription", lastStepExecution.getExitStatus().getExitDescription());
            String outputContent = FreemarkerUtil.parse(templateContent, root);
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage);
            try {
                mimeMessageHelper.setFrom("um-sit@weshareholdings.com.cn");
                mimeMessageHelper.setTo(new String[]{"v_tianwenkai@weshareholdings.com", "v_shengchongyang@weshareholdings.com", "jiayuan.qu@weshareholdings.com"});
                mimeMessageHelper.setSubject("Job异常信息明细");
                mimeMessageHelper.setText(outputContent, true);
                mailSender.send(mimeMailMessage);
                log.info("邮件发送成功：{}", jobExecution.getJobInstance().getJobName());
            } catch (MessagingException e) {
                e.printStackTrace();
                log.error("邮件发送失败：{}", jobExecution.getJobInstance().getJobName());
            }
        }
    }
}

