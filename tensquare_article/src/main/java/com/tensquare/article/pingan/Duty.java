package com.tensquare.article.pingan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-05-09 23:04:23
 * @describe: 税务DTO
 */
public class Duty implements Serializable {
    private static final long serialVersionUID = -8878991264886225318L;
    private String duityId;
    private String dutyName;/** 税务局名称 */
    private String workCompany;/** 受雇单位 */
    private String idCard;/** 征税人身份证号 */
    private String payStart;/** 缴纳起始年度 */
    private String payEnd;/** 缴纳终止年度 */
    private Date applyDate;/** 申请时间 */
    private Date auditDate;/** 审核时间 */
    private Date askDate;/** 咨询时间 */
    private String askResult;/** 咨询结果 */
    private BigDecimal applyAuditDays;/** 申请到审核之间的天数 */
    private String auditStatus;/** 审核状态0-审核失败,1-审核成功 */
    private Date dealDate;/** 国库处理时间 */
    private BigDecimal auditDealDays;/** 审核到国库处理的天数 */
    private String dealStatus;/** 处理状态 0-处理失败,1-处理成功 */
    private BigDecimal salaryCount;/** 收入合计 */
    private BigDecimal taxPayAmount;/** 已缴纳税额 */
    private BigDecimal freeDutyAmount;/** 免税收入 */
    private String freeDutyName;/** 减免税项目 */
    private String dutyStatus;/** 税收状态 0-退税,1-补税 */
    private BigDecimal dutySubtractAmount;/** 退税补税差价 */


    public String getDuityId() {
        return duityId;
    }

    public void setDuityId(String duityId) {
        this.duityId = duityId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPayStart() {
        return payStart;
    }

    public void setPayStart(String payStart) {
        this.payStart = payStart;
    }

    public String getPayEnd() {
        return payEnd;
    }

    public void setPayEnd(String payEnd) {
        this.payEnd = payEnd;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public BigDecimal getApplyAuditDays() {
        return applyAuditDays;
    }

    public void setApplyAuditDays(BigDecimal applyAuditDays) {
        this.applyAuditDays = applyAuditDays;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public BigDecimal getAuditDealDays() {
        return auditDealDays;
    }

    public void setAuditDealDays(BigDecimal auditDealDays) {
        this.auditDealDays = auditDealDays;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public BigDecimal getSalaryCount() {
        return salaryCount;
    }

    public void setSalaryCount(BigDecimal salaryCount) {
        this.salaryCount = salaryCount;
    }

    public BigDecimal getTaxPayAmount() {
        return taxPayAmount;
    }

    public void setTaxPayAmount(BigDecimal taxPayAmount) {
        this.taxPayAmount = taxPayAmount;
    }

    public BigDecimal getFreeDutyAmount() {
        return freeDutyAmount;
    }

    public void setFreeDutyAmount(BigDecimal freeDutyAmount) {
        this.freeDutyAmount = freeDutyAmount;
    }

    public String getFreeDutyName() {
        return freeDutyName;
    }

    public void setFreeDutyName(String freeDutyName) {
        this.freeDutyName = freeDutyName;
    }

    public String getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(String dutyStatus) {
        this.dutyStatus = dutyStatus;
    }

    public BigDecimal getDutySubtractAmount() {
        return dutySubtractAmount;
    }

    public void setDutySubtractAmount(BigDecimal dutySubtractAmount) {
        this.dutySubtractAmount = dutySubtractAmount;
    }

    public Date getAskDate() {
        return askDate;
    }

    public void setAskDate(Date askDate) {
        this.askDate = askDate;
    }

    public String getAskResult() {
        return askResult;
    }

    public void setAskResult(String askResult) {
        this.askResult = askResult;
    }
}
