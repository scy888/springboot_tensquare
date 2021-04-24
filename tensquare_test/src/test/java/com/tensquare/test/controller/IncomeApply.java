package com.tensquare.test.controller;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author v_tianwenkai
 * @since 2020/12/18 11:04
 */
@Data
@Document(collection = "msg_log_info")
public class IncomeApply implements Serializable {

    @Id
    private String id;
    private String partner;
    private String projectNo;
    private String service;
    private EnterReqParam enterReqParam;
    private EnterRespParam enterRespParam;
    private OutReqParam outReqParam;
    private OutRespParam outRespParam;
    private String requestTime;
    private String createTime;
    private String updateTime;

    @Data
    public static class EnterReqParam {
        private String service;
        private String partner;
        private String projectNo;
        private String serviceSn;
        private String timeStamp;
        private String content;
        private String sign;
    }

    @Data
    public static class EnterRespParam {
        private String retCode;
        private String retMsg;
        private String rspData;
    }

    @Data
    public static class OutReqParam {
        private String entNo;
        private String projectId;
        private String projectName;
        private String brNo;
        private String projectNo;
        private String timeStamp;
        private String reqData;
        private String sign;
    }

    @Data
    public static class OutRespParam {
        private String retCode;
        private String retMsg;
        private String rspData;
    }

}
