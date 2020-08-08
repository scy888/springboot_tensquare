package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.controller
 * @date: 2020-08-08 12:58:39
 * @describe:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CsvControllerTest {
 @Autowired
 private CsvController csvController;
 @Autowired
 private JdbcTemplate jdbcTemplate;
    @Test
    public void saveRepaymentPlan() throws Exception {
        String strJson = "[\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060315434201160683\",\n" +
                "    \"effectDate\": \"2020-06-03\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 3,\n" +
                "    \"termDueDate\": \"2020-09-12\",\n" +
                "    \"termGraceDate\": \"2020-09-15\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-08-12\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 1.87,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 10.59\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060315434201160683\",\n" +
                "    \"effectDate\": \"2020-06-03\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 9,\n" +
                "    \"termDueDate\": \"2021-03-12\",\n" +
                "    \"termGraceDate\": \"2021-03-15\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2021-02-12\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 0.33,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 12.13\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501004425604\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 1,\n" +
                "    \"termDueDate\": \"2020-07-05\",\n" +
                "    \"termGraceDate\": \"2020-07-08\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-06-04\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 0.14,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 5\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501004691039\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 2,\n" +
                "    \"termDueDate\": \"2020-08-03\",\n" +
                "    \"termGraceDate\": \"2020-08-06\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-07-03\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 1.6,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 26.33\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501016198387\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 1,\n" +
                "    \"termDueDate\": \"2020-06-15\",\n" +
                "    \"termGraceDate\": \"2020-06-18\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-06-04\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 0.04,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 2.88\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501103686498\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 2,\n" +
                "    \"termDueDate\": \"2020-07-15\",\n" +
                "    \"termGraceDate\": \"2020-07-18\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-06-15\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 5.31,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 34.28\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501103686498\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 3,\n" +
                "    \"termDueDate\": \"2020-08-15\",\n" +
                "    \"termGraceDate\": \"2020-08-18\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-07-15\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 4.31,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 35.28\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501103686498\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 5,\n" +
                "    \"termDueDate\": \"2020-10-15\",\n" +
                "    \"termGraceDate\": \"2020-10-18\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-09-15\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 2.22,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 37.37\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501107708717\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 1,\n" +
                "    \"termDueDate\": \"2020-07-14\",\n" +
                "    \"termGraceDate\": \"2020-07-17\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-06-04\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 0.06,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 1.88\n" +
                "  },\n" +
                "  {\n" +
                "    \"batchDate\": \"2020-06-06\",\n" +
                "    \"dueBillNo\": \"1120060420501133914005\",\n" +
                "    \"effectDate\": \"2020-06-04\",\n" +
                "    \"partnerNo\": \"0006\",\n" +
                "    \"projectNo\": \"WS0006200001\",\n" +
                "    \"term\": 1,\n" +
                "    \"termDueDate\": \"2020-07-06\",\n" +
                "    \"termGraceDate\": \"2020-07-09\",\n" +
                "    \"termReduceFee\": 0,\n" +
                "    \"termReduceInt\": 0,\n" +
                "    \"termReducePenalty\": 0,\n" +
                "    \"termReducePrin\": 0,\n" +
                "    \"termRepayFee\": 0,\n" +
                "    \"termRepayInt\": 0,\n" +
                "    \"termRepayPenalty\": 0,\n" +
                "    \"termRepayPrin\": 0,\n" +
                "    \"termStartDate\": \"2020-06-04\",\n" +
                "    \"termStatus\": \"N\",\n" +
                "    \"termTermFee\": 0,\n" +
                "    \"termTermInt\": 0.36,\n" +
                "    \"termTermPenalty\": 0,\n" +
                "    \"termTermPrin\": 12\n" +
                "  }\n" +
                "]";
        FileInputStream fis=new FileInputStream("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources\\csv.json");
        FileOutputStream fos=new FileOutputStream("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources\\csvCopy.json");
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            //System.out.println(new String(bytes, 0, len));
            fos.write(bytes, 0, len);
            fos.flush();
        }

        List<LxgmRepaymentPlanReq> repaymentPlanReqList = JSON.parseArray(strJson, LxgmRepaymentPlanReq.class);
        //log.info("repaymentPlanReqList:{}",JSON.toJSONString(repaymentPlanReqList));
        jdbcTemplate.update("delete from lxgm_repayment_plan where (due_bill_no,term) in ("
                +repaymentPlanReqList.stream().map(e->"('"+e.getDueBillNo()+"','"+e.getTerm()+"')").collect(Collectors.joining(","))+")");
        csvController.saveRepaymentPlan(repaymentPlanReqList);
    }
}