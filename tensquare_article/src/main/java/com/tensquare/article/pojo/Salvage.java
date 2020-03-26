package com.tensquare.article.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-23 00:24:43
 * @describe: 残值
 */
public class Salvage implements Serializable {

    private String salvageId; /** 主键 */
    private String seriaNo;/** 序列号 */
    private String salvageName;/** 残值名称 */
    private String statusCode;/** 状态代码 */
    private String statusValue; /** 状态值 */
    private Date damageStarDate;/** 受损时间 */
    private BigDecimal damageDay; /** 受损天数 */
    private Date damageEndDate;/** 受损截止日期 */
    private String salvageDesc; /** 残值描叙 */
    private String salvageWeb;/** 残值网址 */

    public String getSalvageWeb() {
        return salvageWeb;
    }

    public void setSalvageWeb(String salvageWeb) {
        this.salvageWeb = salvageWeb;
    }

    public String getSalvageId() {
        return salvageId;
    }

    public void setSalvageId(String salvageId) {
        this.salvageId = salvageId;
    }

    public String getSeriaNo() {
        return seriaNo;
    }

    public void setSeriaNo(String seriaNo) {
        this.seriaNo = seriaNo;
    }

    public String getSalvageName() {
        return salvageName;
    }

    public void setSalvageName(String salvageName) {
        this.salvageName = salvageName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public Date getDamageStarDate() {
        return damageStarDate;
    }

    public void setDamageStarDate(Date damageStarDate) {
        this.damageStarDate = damageStarDate;
    }

    public BigDecimal getDamageDay() {
        return damageDay;
    }

    public void setDamageDay(BigDecimal damageDay) {
        this.damageDay = damageDay;
    }

    public Date getDamageEndDate() {
        return damageEndDate;
    }

    public void setDamageEndDate(Date damageEndDate) {
        this.damageEndDate = damageEndDate;
    }

    public String getSalvageDesc() {
        return salvageDesc;
    }

    public void setSalvageDesc(String salvageDesc) {
        this.salvageDesc = salvageDesc;
    }
}
