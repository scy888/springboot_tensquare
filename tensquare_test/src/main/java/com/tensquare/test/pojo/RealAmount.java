package com.tensquare.test.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-08-23 12:24:31
 * @describe:
 */
@Data
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@javax.persistence.Table(indexes = {
        @Index(name = "real_due_bill_no_and_term", unique = true, columnList = "dueBillNo, term")
})
@Table(appliesTo = "real_amount",comment = "实际还款信息")
public class RealAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(32) not null comment '借据号'")
    private String dueBillNo;
    @Column(columnDefinition = "int not null comment '期数'")
    private int term;
    @Column(columnDefinition = "date not null comment '每期还款日期'")
    private LocalDate batchDate;
    @Column(columnDefinition = "decimal(12,2) not null comment '每期还款(元)'")
    private BigDecimal termAmount;
    @CreatedDate
    @Column(columnDefinition = "datetime not null comment '创建时间'",updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(columnDefinition = "datetime not null comment '最后修改时间'")
    private LocalDateTime modifyDate;
}
