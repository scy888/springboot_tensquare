package com.tensquare.article.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-06 13:55:43
 * @describe:
 */
public class Option implements Serializable {
    private String optionId;
    private String spectionId;
    private String optionName;
    private String optionStatus;
    private String optionDesc;
    private Date optionDate;
    //private Spection spection;
    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getSpetionId() {
        return spectionId;
    }

    public void setSpetionId(String spetionId) {
        this.spectionId = spetionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionStatus() {
        return optionStatus;
    }

    public void setOptionStatus(String optionStatus) {
        this.optionStatus = optionStatus;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

    public void setOptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }

    public Date getOptionDate() {
        return optionDate;
    }

    public void setOptionDate(Date optionDate) {
        this.optionDate = optionDate;
    }

   /* public Spection getSpection() {
        return spection;
    }

    public void setSpection(Spection spection) {
        this.spection = spection;
    }*/

}

