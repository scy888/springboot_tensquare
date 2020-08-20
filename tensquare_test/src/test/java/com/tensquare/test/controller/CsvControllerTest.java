package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.test.dao.CsvDao;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
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
    @Autowired
    private CsvDao csvDao;
    private String jsonStr = null;

    @Before
    public void init() {

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource("csv.json").toURI()));
            jsonStr = new String(bytes, "utf-8");
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("json字符串解析失败:{}", e.getMessage());
        }
    }

    @After
    public void destory() throws Exception {
        FileInputStream fis = new FileInputStream("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources\\csv.json");
        FileOutputStream fos = new FileOutputStream("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources\\csvCopy.json");
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            //System.out.println(new String(bytes, 0, len));
            fos.write(bytes, 0, len);
            fos.flush();
        }
    }
    @Test
    public void saveRepaymentPlan() throws Exception {
        List<LxgmRepaymentPlanReq> repaymentPlanReqList = JSON.parseArray(jsonStr, LxgmRepaymentPlanReq.class);
        //log.info("repaymentPlanReqList:{}",JSON.toJSONString(repaymentPlanReqList));
        jdbcTemplate.update("delete from lxgm_repayment_plan where (due_bill_no,term) in ("
                + repaymentPlanReqList.stream().map(e -> "('" + e.getDueBillNo() + "','" + e.getTerm() + "')").collect(Collectors.joining(",")) + ")");
        csvController.saveRepaymentPlan(repaymentPlanReqList);
    }
    @Test
    public void test(){
        List<LxgmRepaymentPlanReq> repaymentPlanReqList = JSON.parseArray(jsonStr, LxgmRepaymentPlanReq.class);
        List<Integer> list = csvDao.selectByDueBillNos(repaymentPlanReqList.stream().map(LxgmRepaymentPlanReq::getDueBillNo).collect(Collectors.toList()));
        log.info("查询出来的list：{}",JSON.toJSONString(list));
    }
}