package com.tensquare.batch.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;

import java.util.Collection;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.pojo
 * @date: 2020-07-11 22:59:10
 * @describe:
 */
@Data
public class Job {
    private Long id;
    private BatchStatus status;
    private ExitStatus exitStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private Collection<Step> stepCollection;
    private Boolean isRunning;
    private Boolean isStopping;
    private JobParameters jobParameters;
}
