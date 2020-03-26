package com.tensquare.article.pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * @author: scyang
 * @program: ssm_super
 * @package: com.itheima.pian.dto
 * @date: 2019-12-04 20:22:52
 * @describe: 风险反馈dto
 */
public class RiskBack implements Serializable {

    private static final long serialVersionUID = -5031612678500850606L;
    private String riskBackId; /** 主键 */
    private String reportNo;/** 报案号 */
    private String statusDesc;
    private String insuredIdCard; /** 被保险人身份证号 */
    private String plateNumber;/** 运输车牌号 */
    private String riskDriveTel; /** 报案手机号 */
    /** 反馈结果值 0-无风险,1-虚报损失,2-保险欺诈,3-其他风险 */
    private String riskBackCode;
    private String riskBackValue;
    private String riskListType; /** 风险名单类型 */
    private Date effectiveDate; /** 生效时间 */
    private Date failureDate; /** 失效时间 */
    private String validFlag;/** 有效标志0-有效,1-移除 */



    public String getRiskBackId() {
        return riskBackId;
    }

    public void setRiskBackId(String riskBackId) {
        this.riskBackId = riskBackId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getInsuredIdCard() {
        return insuredIdCard;
    }

    public void setInsuredIdCard(String insuredIdCard) {
        this.insuredIdCard = insuredIdCard;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getRiskDriveTel() {
        return riskDriveTel;
    }

    public void setRiskDriveTel(String riskDriveTel) {
        this.riskDriveTel = riskDriveTel;
    }

    public String getRiskBackValue() {
        return riskBackValue;
    }

    public void setRiskBackValue(String riskBackValue) {
        this.riskBackValue = riskBackValue;
    }



    public String getRiskBackCode() {
        return riskBackCode;
    }

    public void setRiskBackCode(String riskBackCode) {
        this.riskBackCode = riskBackCode;
    }

    public String getRiskListType() {
        return riskListType;
    }

    public void setRiskListType(String riskListType) {
        this.riskListType = riskListType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getFailureDate() {
        return failureDate;
    }

    public void setFailureDate(Date failureDate) {
        this.failureDate = failureDate;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
