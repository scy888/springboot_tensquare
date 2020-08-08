package com.tensquare.req;

import com.tensquare.result.LxgmTermStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LxgmRepaymentPlanReq {

    private String projectNo;

    private String partnerNo;

    private String dueBillNo;

    private Integer term;

    private LocalDate termStartDate;

    private LocalDate termDueDate;

    private LocalDate termGraceDate;

    private BigDecimal termTermPrin;

    private BigDecimal termTermInt;

    private BigDecimal termTermPenalty;

    private BigDecimal termTermFee;

    private BigDecimal termRepayPrin;

    private BigDecimal termRepayInt;

    private BigDecimal termRepayPenalty;

    private BigDecimal termRepayFee;

    private BigDecimal termReducePrin;

    private BigDecimal termReduceInt;

    private BigDecimal termReducePenalty;

    private BigDecimal termReduceFee;

    private LxgmTermStatus termStatus;

    private LocalDate settleDate;

    private LocalDate effectDate;

    /**
     * 跑批的日期
     */
    private LocalDate batchDate;

}
