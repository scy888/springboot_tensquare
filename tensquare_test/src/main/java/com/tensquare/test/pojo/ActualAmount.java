package com.tensquare.test.pojo;

import com.tensquare.result.LxgmTermStatus;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-08-23 12:24:31
 * @describe:
 */
@Data
@Entity
//@Accessors(chain = true)
@Table(appliesTo = "actual_amount",comment = "实际还款信息")
public class ActualAmount {
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
    @Column(columnDefinition = "varchar(2) not null comment '还款状态 N-正常,O-逾期,F-结清'")
    private String status;
}
