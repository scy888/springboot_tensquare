package com.tensquare.article.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-06 13:49:33
 * @describe: 规格列表
 */
public class Spection implements Serializable {
    private String spectionId;
    private String spectionName;
    private String spectionCode;
    private Date spectionDate;
    private Map<String, List<Option>> optionMap;

    public String getSpectionId() {
        return spectionId;
    }

    public void setSpectionId(String spectionId) {
        this.spectionId = spectionId;
    }

    public String getSpectionName() {
        return spectionName;
    }

    public void setSpectionName(String spectionName) {
        this.spectionName = spectionName;
    }

    public String getSpectionCode() {
        return spectionCode;
    }

    public void setSpectionCode(String spectionCode) {
        this.spectionCode = spectionCode;
    }

    public Date getSpectionDate() {
        return spectionDate;
    }

    public void setSpectionDate(Date spectionDate) {
        this.spectionDate = spectionDate;
    }

    public Map<String, List<Option>> getOptionMap() {
        return optionMap;
    }

    public void setOptionMap(Map<String, List<Option>> optionMap) {
        this.optionMap = optionMap;
    }

}
