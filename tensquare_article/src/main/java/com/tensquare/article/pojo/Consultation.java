package com.tensquare.article.pojo;
import java.io.Serializable;

/**
 * @author: scyang
 * @program: ssm_super
 * @package: com.itheima.pian.dto
 * @date: 2019-12-14 20:41:29
 * @describe: 合议人员表
 */
public class Consultation implements Serializable {

    private static final long serialVersionUID = 7255893731514247377L;
    /** 主键 */
    private String consultationId;
    /** 合议人(UM账号) */
    private String consultationUm;
    /** 合议人姓名 */
    private String consultationName;
    /** 合议人电话 */
    private String phone;
    /** 所属合议类别 1-机构合议人,2-总部合议人,3-核保合议人,4-法务合议人,5-再保合议人6-精算合议人 */
    private String consultationType;
    /** 角色 */
    private String role;
    /** 邮件顺序 */
    private String mailOrder;

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getConsultationUm() {
        return consultationUm;
    }

    public void setConsultationUm(String consultationUm) {
        this.consultationUm = consultationUm;
    }

    public String getConsultationName() {
        return consultationName;
    }

    public void setConsultationName(String consultationName) {
        this.consultationName = consultationName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMailOrder() {
        return mailOrder;
    }

    public void setMailOrder(String mailOrder) {
        this.mailOrder = mailOrder;
    }
}
