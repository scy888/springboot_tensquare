package com.tensquare.article.pingan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-05-10 11:10:30
 * @describe:
 */
public class DutyPerson implements Serializable {
    private static final long serialVersionUID = -8965844511596401638L;
    private String dutyPersonId;
    private String personName;
    private String workCompany;
    private BigDecimal salaryAmount;
    private String freeDutyName;
    private String idCard;

    public String getDutyPersonId() {
        return dutyPersonId;
    }

    public void setDutyPersonId(String dutyPersonId) {
        this.dutyPersonId = dutyPersonId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany;
    }

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getFreeDutyName() {
        return freeDutyName;
    }

    public void setFreeDutyName(String freeDutyName) {
        this.freeDutyName = freeDutyName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
