package com.tensquare.article.pingan;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-04-25 15:17:17
 * @describe: 雇员信息
 */
public class Employer implements Serializable {
    private static final long serialVersionUID = 1653775260067886408L;
    private String emploverId;
    private String names;
    private String idCard;
    private String seriaNo;
    private String email;
    private Date startDate;
    private Date endDate;
    private String types;
    private Map<String, List<Employer>> dataMap;

    public String getEmploverId() {
        return emploverId;
    }

    public void setEmploverId(String emploverId) {
        this.emploverId = emploverId;
    }



    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSeriaNo() {
        return seriaNo;
    }

    public void setSeriaNo(String seriaNo) {
        this.seriaNo = seriaNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Map<String, List<Employer>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<Employer>> dataMap) {
        this.dataMap = dataMap;
    }
}
