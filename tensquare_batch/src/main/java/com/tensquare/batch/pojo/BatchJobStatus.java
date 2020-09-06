package com.tensquare.batch.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.pojo
 * @date: 2020-09-06 21:09:24
 * @describe:
 */
@Data
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BatchJobStatus implements Serializable {
    private static final long serialVersionUID = 7170555621019836198L;

    @Id
    private String id;
    @Column(columnDefinition = "varchar(50) not null comment '任务名称'")
    private String jobName;
    private Boolean isRunning;
    @CreatedDate
    private LocalDateTime createDate;
}
