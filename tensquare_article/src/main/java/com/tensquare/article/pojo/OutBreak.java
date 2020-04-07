package com.tensquare.article.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-04-07 19:52:00
 * @describe: 武汉疫情信息表
 */
public class OutBreak implements Serializable {

    private static final long serialVersionUID = -4681486622769086923L;
    private String outBreakId;
    /** 支援城市 */
    private String supportCity;
    /** 医院名称 */
    private String hospitalName;
    /** 所属城市 */
    private String ownCity;
    /** 抵达时间 */
    private Date arriveDate;
    /** 撤离时间 */
    private Date leaveDate;
    /** 支援天数 */
    private BigDecimal supportDays;
    private int nurseCount;
    /** 补贴金额 */
    private BigDecimal subsidyAmount;
    /** 补贴总金额 */
    private BigDecimal subsidySum;
    private String rate;


    public String getOutBreakId() {
        return outBreakId;
    }

    public void setOutBreakId(String outBreakId) {
        this.outBreakId = outBreakId;
    }

    public String getSupportCity() {
        return supportCity;
    }

    public void setSupportCity(String supportCity) {
        this.supportCity = supportCity;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getOwnCity() {
        return ownCity;
    }

    public void setOwnCity(String ownCity) {
        this.ownCity = ownCity;
    }

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public BigDecimal getSupportDays() {
        return supportDays;
    }

    public void setSupportDays(BigDecimal supportDays) {
        this.supportDays = supportDays;
    }

    public int getNurseCount() {
        return nurseCount;
    }

    public void setNurseCount(int nurseCount) {
        this.nurseCount = nurseCount;
    }

    public BigDecimal getSubsidyAmount() {
        return subsidyAmount;
    }

    public void setSubsidyAmount(BigDecimal subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public BigDecimal getSubsidySum() {
        return subsidySum;
    }

    public void setSubsidySum(BigDecimal subsidySum) {
        this.subsidySum = subsidySum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
