package com.tensquare.article.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-13 17:07:39
 * @describe:
 */
public class Policy implements Serializable {
    /** 主键 */
   private String policyId;
   /** 保单号 */
   private String policyNo;
   /** 保险单位 */
  private String insuredName;
  /** 出险原因 */
  private String accidentReason;
  /** 出险时间 */
  private Date accidentDate;
  /** 案件状态 */
  private String caseStatus;
  /** 案件状态值 */
  private String caseValue;
  /** 已决金额 */
  private BigDecimal settlendAmount;
  /** 未决金额 */
  private BigDecimal outstandAmount;
  /** 赔付金额 */
  private BigDecimal payAmount;
  /** 币种 */
  private String currencyCode;
  /** 币种描叙 */
  private String currencyDesc;
  /** 汇率 */
  private BigDecimal exchangeRate;
 /** 未决金额所占比例 */
  private String settlendRate;
  /** 未决金额所占比例 */
  private String outstandRate;

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getAccidentReason() {
        return accidentReason;
    }

    public void setAccidentReason(String accidentReason) {
        this.accidentReason = accidentReason;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(Date accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseValue() {
        return caseValue;
    }

    public void setCaseValue(String caseValue) {
        this.caseValue = caseValue;
    }

    public BigDecimal getSettlendAmount() {
        return settlendAmount;
    }

    public void setSettlendAmount(BigDecimal settlendAmount) {
        this.settlendAmount = settlendAmount;
    }

    public BigDecimal getOutstandAmount() {
        return outstandAmount;
    }

    public void setOutstandAmount(BigDecimal outstandAmount) {
        this.outstandAmount = outstandAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getSettlendRate() {
        return settlendRate;
    }

    public void setSettlendRate(String settlendRate) {
        this.settlendRate = settlendRate;
    }

    public String getOutstandRate() {
        return outstandRate;
    }

    public void setOutstandRate(String outstandRate) {
        this.outstandRate = outstandRate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Policy.class.getSimpleName() + "[", "]")
                .add("policyId='" + policyId + "'")
                .add("policyNo='" + policyNo + "'")
                .add("insuredName='" + insuredName + "'")
                .add("accidentReason='" + accidentReason + "'")
                .add("accidentDate=" + accidentDate)
                .add("caseStatus='" + caseStatus + "'")
                .add("caseValue='" + caseValue + "'")
                .add("settlendAmount=" + settlendAmount)
                .add("outstandAmount=" + outstandAmount)
                .add("payAmount=" + payAmount)
                .add("currencyCode='" + currencyCode + "'")
                .add("currencyDesc='" + currencyDesc + "'")
                .add("exchangeRate=" + exchangeRate)
                .add("settlendRate='" + settlendRate + "'")
                .add("outstandRate='" + outstandRate + "'")
                .toString();
    }
}
