package com.tensquare.batch.pojo;

import com.tensquare.result.LxgmTermStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 还款计划信息表,05.scv
 *
 * @author chongyang.sheng
 * @version 1.0
 * @since 2020/6/19 18:38
 **/
@Data
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(name = "repayment_plan_due_bill_no_and_term", unique = true, columnList = "dueBillNo, term"),
        @Index(name = "repayment_plan_batch_date", columnList = "batchDate")
},
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"dueBillNo", "term"})
        })
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "repayment_plan", comment = "还款计划信息表")
public class RepaymentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(columnDefinition = "varchar(32) not null comment '项目id'")
    private Integer id;

    @Column(columnDefinition = "varchar(32) not null comment '项目编号'")
    private String projectNo;

    @Column(columnDefinition = "varchar(10) not null comment '机构编号'")
    private String partnerNo;

    @Column(columnDefinition = "varchar(100) not null comment '借据号'")
    private String dueBillNo;

    @Column(columnDefinition = "int not null comment '每笔'")
    private Integer term;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(2) null comment '还款状态 N-正常,O-逾期,F-结清'")
    private LxgmTermStatus termStatus;

    @Column(columnDefinition = "date null comment '跑批日期'")
    private LocalDate batchDate;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


    /**
     * 对象连接的字符串，去掉了id、createdDate、lastModifiedDate（为了提升有效更新，先判断两个对象的joinedString是否一样）
     *
     * @return
     */


}
