package com.tensquare.batch.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.pojo
 * @date: 2020-07-11 22:58:09
 * @describe:
 */
@Data
public class Step {
    private Long id;
    private BatchStatus status;
    private int commitCount;
    private int readCount;
    private int writeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastDate;
    private ExitStatus exitStatus;
    private String stepName;
}
