package com.tensquare.test.pojo;

import com.tensquare.result.LxgmTermStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        @Index(name = "lxgm_repayment_plan_due_bill_no_and_term", unique = true, columnList = "dueBillNo, term"),
        @Index(name = "lxgm_repayment_plan_batch_date", columnList = "batchDate")
},
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"dueBillNo", "term"})
        })
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "lxgm_repayment_plan", comment = "还款计划信息表")
public class LxgmRepaymentPlan {
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

    @Column(columnDefinition = "date not null comment '计息开始日'")
    private LocalDate termStartDate;

    @Column(columnDefinition = "date not null comment '应还款日'")
    private LocalDate termDueDate;

    @Column(columnDefinition = "date not null comment '宽限日到期日'")
    private LocalDate termGraceDate;

    @Column(columnDefinition = "decimal(12,2) not null comment '应还本金(元)'")
    private BigDecimal termTermPrin;

    @Column(columnDefinition = "decimal(12,2) not null comment '应还利息(元)'")
    private BigDecimal termTermInt;

    @Column(columnDefinition = "decimal(12,2) not null comment '应还罚息(元)'")
    private BigDecimal termTermPenalty;

    @Column(columnDefinition = "decimal(12,2) not null comment '应还费用(元)'")
    private BigDecimal termTermFee;

    @Column(columnDefinition = "decimal(12,2) not null comment '已还本金(元)'")
    private BigDecimal termRepayPrin;

    @Column(columnDefinition = "decimal(12,2) not null comment '已还利息(元)'")
    private BigDecimal termRepayInt;

    @Column(columnDefinition = "decimal(12,2) not null comment '已还罚息(元)'")
    private BigDecimal termRepayPenalty;

    @Column(columnDefinition = "decimal(12,2) not null comment '已还费用(元)'")
    private BigDecimal termRepayFee;

    @Column(columnDefinition = "decimal(12,2) not null comment '减免本金(元)'")
    private BigDecimal termReducePrin;

    @Column(columnDefinition = "decimal(12,2) not null comment '减免利息(元)'")
    private BigDecimal termReduceInt;

    @Column(columnDefinition = "decimal(12,2) not null comment '减免罚息(元)'")
    private BigDecimal termReducePenalty;

    @Column(columnDefinition = "decimal(12,2) not null comment '减免费用(元)'")
    private BigDecimal termReduceFee;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(2) not null comment '还款状态 N-正常,O-逾期,F-结清'")
    private LxgmTermStatus termStatus;

    @Column(columnDefinition = "date  null comment '结清日期'")
    private LocalDate settleDate;

    @Column(columnDefinition = "date not null comment '生效日期'")
    private LocalDate effectDate;

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
    public String joinedString() {
        final StringBuilder sb = new StringBuilder("LxgmRepaymentPlan{");
        sb.append("projectNo='").append(projectNo).append('\'');
        sb.append(", partnerNo='").append(partnerNo).append('\'');
        sb.append(", dueBillNo='").append(dueBillNo).append('\'');
        sb.append(", term=").append(term);
        sb.append(", termStartDate=").append(termStartDate);
        sb.append(", termDueDate=").append(termDueDate);
        sb.append(", termGraceDate=").append(termGraceDate);
        sb.append(", termTermPrin=").append(termTermPrin.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termTermInt=").append(termTermInt.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termTermPenalty=").append(termTermPenalty.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termTermFee=").append(termTermFee.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termRepayPrin=").append(termRepayPrin.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termRepayInt=").append(termRepayInt.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termRepayPenalty=").append(termRepayPenalty.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termRepayFee=").append(termRepayFee.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termReducePrin=").append(termReducePrin.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termReduceInt=").append(termReduceInt.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termReducePenalty=").append(termReducePenalty.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termReduceFee=").append(termReduceFee.setScale(2, RoundingMode.HALF_UP));
        sb.append(", termStatus=").append(termStatus);
        sb.append(", settleDate=").append(settleDate);
        sb.append(", effectDate=").append(effectDate);
        sb.append('}');
        return sb.toString();
    }

}
