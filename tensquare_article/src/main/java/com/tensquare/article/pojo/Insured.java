package com.tensquare.article.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-16 19:59:43
 * @describe: 保险人
 */
public class Insured implements Serializable {
    private String insuredId;
    private String policyNo;
    private String insuredName;
    private String idCard;
    private String iphone;
    private String sex;
    private Date birthday;
    private Integer age;
    private String conclusionCode;
    private String conclusionValue;
    private String conclusionReason;
    private String insuredDesc;
    private Map<String,Object> map;

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getConclusionCode() {
        return conclusionCode;
    }

    public void setConclusionCode(String conclusionCode) {
        this.conclusionCode = conclusionCode;
    }

    public String getConclusionValue() {
        return conclusionValue;
    }

    public void setConclusionValue(String conclusionValue) {
        this.conclusionValue = conclusionValue;
    }

    public String getConclusionReason() {
        return conclusionReason;
    }

    public void setConclusionReason(String conclusionReason) {
        this.conclusionReason = conclusionReason;
    }

    public String getInsuredDesc() {
        return insuredDesc;
    }

    public void setInsuredDesc(String insuredDesc) {
        this.insuredDesc = insuredDesc;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(String insuredId) {
        this.insuredId = insuredId;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
