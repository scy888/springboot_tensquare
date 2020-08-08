package com.tensquare.test.enums;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-19 17:02:44
 * @describe:
 */

public enum ExceptionEnum {
    /** 签名错误 */
    SIGN_ERROR("30001","签名错误"),
    PARAMS_ERROR("30002","参数校验错误"),
    SUCCESS("20000","参数校验成功");
    private String retCode;
    private String retMessage;

    public String getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    ExceptionEnum(String retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }
}
