package com.tensquare.article.pingan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-05-12 20:00:13
 * @describe: 重案信息表
 */
public class CasePerson implements Serializable {
    private static final long serialVersionUID = -2668681408191524170L;
    private String casePersonId;
    private String caseTeamId;
    private String casePersonName;
    private BigDecimal totalScore;
    private BigDecimal effectiveScore;
    private BigDecimal serviceScore;
    private BigDecimal contributeScore;
    private Date createDate;

    public String getCasePersonId() {
        return casePersonId;
    }

    public void setCasePersonId(String casePersonId) {
        this.casePersonId = casePersonId;
    }

    public String getCaseTeamId() {
        return caseTeamId;
    }

    public void setCaseTeamId(String caseTeamId) {
        this.caseTeamId = caseTeamId;
    }

    public String getCasePersonName() {
        return casePersonName;
    }

    public void setCasePersonName(String casePersonName) {
        this.casePersonName = casePersonName;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getEffectiveScore() {
        return effectiveScore;
    }

    public void setEffectiveScore(BigDecimal effectiveScore) {
        this.effectiveScore = effectiveScore;
    }

    public BigDecimal getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(BigDecimal serviceScore) {
        this.serviceScore = serviceScore;
    }

    public BigDecimal getContributeScore() {
        return contributeScore;
    }

    public void setContributeScore(BigDecimal contributeScore) {
        this.contributeScore = contributeScore;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
