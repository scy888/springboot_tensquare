package com.tensquare.article.pojo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-03-01 13:44:23
 * @describe: 邮件实体类
 */
@ApiModel("邮件实体类")
public class Email implements Serializable {
    @ApiModelProperty("邮件主题")
    private String subject;
    @ApiModelProperty("邮件类容")
    private String message;
    @ApiModelProperty("发件人um账号")
    private String sendUm;
    @ApiModelProperty("发件时间")
    private Date sendDate;
    @ApiModelProperty("收件人列表")
    private List<String> toList;
    @ApiModelProperty("抄送人列表")
    private List<String> ccList;
    @ApiModelProperty("收件人姓名")
    private String toName;
    @ApiModelProperty("抄送人姓名")
    private String ccName;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendUm() {
        return sendUm;
    }

    public void setSendUm(String sendUm) {
        this.sendUm = sendUm;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

}
