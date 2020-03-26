package com.tensquare.article.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-02-04 01:16:06
 * @describe:
 */

/*@Entity
@Table(name = "tb_city")*/
public class City implements Serializable {

    private String cityId;
    private String provinceId;
    private String cityName;
    private String cityCode;
    private String cityShort;
    private String cityDesc;
    private Date cityDate;
    /*private Province province;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }*/
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityShort() {
        return cityShort;
    }

    public void setCityShort(String cityShort) {
        this.cityShort = cityShort;
    }

    public String getCityDesc() {
        return cityDesc;
    }

    public void setCityDesc(String cityDesc) {
        this.cityDesc = cityDesc;
    }

    public Date getCityDate() {
        return cityDate;
    }

    public void setCityDate(Date cityDate) {
        this.cityDate = cityDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", City.class.getSimpleName() + "[", "]")
                .add("cityId='" + cityId + "'")
                .add("provinceId='" + provinceId + "'")
                .add("cityName='" + cityName + "'")
                .add("cityCode='" + cityCode + "'")
                .add("cityShort='" + cityShort + "'")
                .add("cityDesc='" + cityDesc + "'")
                .add("cityDate=" + cityDate)
                .toString();
    }
}

