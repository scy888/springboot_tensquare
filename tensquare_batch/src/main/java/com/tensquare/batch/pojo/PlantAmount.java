package com.tensquare.batch.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-08-23 12:52:03
 * @describe:
 */
@Data
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@javax.persistence.Table(indexes = {
        @Index(name = "plan_due_bill_no", unique = true, columnList = "dueBillNo")
})
@Table(appliesTo = "plant_amount",comment = "计划还款信息")
public class PlantAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(32) not null comment '借据号'")
    private String dueBillNo;
    @Column(columnDefinition = "date not null comment '最终还款日期'")
    private LocalDate batchDate;
    @Column(columnDefinition = "decimal(12,2) default 0.00 comment '实际还款(元)'")
    private BigDecimal totalAmount;
    @CreatedDate
    @Column(columnDefinition = "datetime not null comment '创建时间'",updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(columnDefinition = "datetime not null comment '最后修改时间'")
    @Transient
    private List<ActualAmount> actualAmounts;
}
