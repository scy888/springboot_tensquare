package com.tensquare.test.pojo;

import com.tensquare.result.LxgmTermStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(appliesTo = "asset_amount",comment = "资产还款信息")
public class AssetAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(32) not null comment '借据号'")
    private String dueBillNo;
    @Column(columnDefinition = "date not null comment '最终还款日期'")
    private LocalDate batchDate;
    @Column(columnDefinition = "decimal(12,2) not null comment '实际还款(元)'")
    private BigDecimal totalAmount;
    @Column(columnDefinition = "varchar(2) not null comment '还款状态 N-正常,O-逾期,F-结清'")
    private String status;
   // private List<ActualAmount> actualAmounts;
}
