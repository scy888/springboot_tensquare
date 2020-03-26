package com.tensquare.article.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-04 01:14:53
 * @describe:
 */

/*@Entity
@Table(name = "tb_province")*/
public class Province implements Serializable {

    private String provinceId;
    private String provinceName;
    private String provinceStatus;
    private String provinceShort;
    private String provinceDesc;
    private Date provinceDate;
    private List<City> cityList;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceStatus() {
        return provinceStatus;
    }

    public void setProvinceStatus(String provinceStatus) {
        this.provinceStatus = provinceStatus;
    }

    public String getProvinceShort() {
        return provinceShort;
    }

    public void setProvinceShort(String provinceShort) {
        this.provinceShort = provinceShort;
    }

    public String getProvinceDesc() {
        return provinceDesc;
    }

    public void setProvinceDesc(String provinceDesc) {
        this.provinceDesc = provinceDesc;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public Date getProvinceDate() {
        return provinceDate;
    }

    public void setProvinceDate(Date provinceDate) {
        this.provinceDate = provinceDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Province.class.getSimpleName() + "[", "]")
                .add("provinceId='" + provinceId + "'")
                .add("provinceName='" + provinceName + "'")
                .add("provinceStatus='" + provinceStatus + "'")
                .add("provinceShort='" + provinceShort + "'")
                .add("provinceDesc='" + provinceDesc + "'")
                .add("provinceDate=" + provinceDate)
                .add("cityList=" + cityList)
                .toString();
    }
}

