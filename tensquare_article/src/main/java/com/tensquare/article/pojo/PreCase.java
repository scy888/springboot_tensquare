package com.tensquare.article.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-03-29 20:23:35
 * @describe:
 */
public class PreCase implements Serializable {
    private static final long serialVersionUID = 162974082842182932L;
    private String preCaseId;
    private BigDecimal money;
    private String isOnLine;
    private String downLineSchedul;
    private String accidentLevel;
    private String deathPeople;
    private String hurtPeople;
    private Date createDate;


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPreCaseId() {
        return preCaseId;
    }

    public void setPreCaseId(String preCaseId) {
        this.preCaseId = preCaseId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(String isOnLine) {
        this.isOnLine = isOnLine;
    }

    public String getDownLineSchedul() {
        return downLineSchedul;
    }

    public void setDownLineSchedul(String downLineSchedul) {
        this.downLineSchedul = downLineSchedul;
    }

    public String getAccidentLevel() {
        return accidentLevel;
    }

    public void setAccidentLevel(String accidentLevel) {
        this.accidentLevel = accidentLevel;
    }

    public String getDeathPeople() {
        return deathPeople;
    }

    public void setDeathPeople(String deathPeople) {
        this.deathPeople = deathPeople;
    }

    public String getHurtPeople() {
        return hurtPeople;
    }

    public void setHurtPeople(String hurtPeople) {
        this.hurtPeople = hurtPeople;
    }

}
