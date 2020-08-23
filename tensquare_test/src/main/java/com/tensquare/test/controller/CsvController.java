package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.result.DataCheckResult;
import com.tensquare.result.LxgmTermStatus;
import com.tensquare.result.Result;
import com.tensquare.test.dao.CsvDao;
import com.tensquare.test.pojo.ActualAmount;
import com.tensquare.test.pojo.AssetAmount;
import com.tensquare.test.pojo.DueBillNoTermVo;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.IdWorker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.controller
 * @date: 2020-08-06 22:54:02
 * @describe:
 */
@RestController
@Slf4j
public class CsvController {
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Autowired
    private CsvDao csvDao;

    @RequestMapping("/saveRepaymentPlan")
    Result saveRepaymentPlan(@RequestBody List<LxgmRepaymentPlanReq> lxgmRepaymentPlanReqs) {
        List<LxgmRepaymentPlan> lxgmRepaymentPlans = lxgmRepaymentPlanReqs.stream().map(lxgmRepaymentPlanReq -> {
            LxgmRepaymentPlan lxgmRepaymentPlan = new LxgmRepaymentPlan();
            BeanUtils.copyProperties(lxgmRepaymentPlanReq, lxgmRepaymentPlan);
            return lxgmRepaymentPlan;
        }).collect(Collectors.toList());
        //log.info("lxgmRepaymentPlans:{}", JSON.toJSONString(lxgmRepaymentPlans));
        List<DueBillNoTermVo> dueBillNoTermVos = lxgmRepaymentPlans.stream().map(lxgmRepaymentPlan -> new DueBillNoTermVo()
                .setDueBillNo(lxgmRepaymentPlan.getDueBillNo())
                .setTerm(lxgmRepaymentPlan.getTerm()))
                .collect(Collectors.toList());

        List<LxgmRepaymentPlan> exsitList = csvDao.selectByDueBillNoAndTerm(dueBillNoTermVos);
        List<LxgmRepaymentPlan> insertList = new ArrayList<>();
        List<LxgmRepaymentPlan> updateList = new ArrayList<>();
        for (LxgmRepaymentPlan repaymentPlan : lxgmRepaymentPlans) {
            if (exsitList.stream().noneMatch(e -> e.getDueBillNo().equals(repaymentPlan.getDueBillNo()) &&
                    e.getTerm().equals(repaymentPlan.getTerm()))) {
                insertList.add(repaymentPlan);
            } else {
                updateList.add(repaymentPlan);
            }
        }
        log.info("insertList的size():{}条,updateList的size():{}条", insertList.size(), updateList.size());
        csvDao.insertList(insertList);
        csvDao.updateList(updateList);
        return Result.ok("success");
    }

    @RequestMapping("/addList")
    public String addList(@RequestBody String jsonParam) {
        String msg = "";
        try {
            log.info("jsonParam:{}", jsonParam);
            JSONObject jsonObject = JSON.parseObject(jsonParam);
            List<ActualAmount> actualList = jsonObject.getJSONArray("actualList").toJavaList(ActualAmount.class);
            List<AssetAmount> assetList = jsonObject.getJSONArray("assetList").toJavaList(AssetAmount.class);

            for (AssetAmount assetAmount : assetList) {
                for (ActualAmount actualAmount : actualList) {
                    if (assetAmount.getDueBillNo().equals(actualAmount.getDueBillNo())) {
                        assetAmount.setBatchDate((LocalDate) getMap(actualList.stream().filter(a -> a.getDueBillNo().equals(assetAmount.getDueBillNo())).collect(Collectors.toList())).get("maxBatchDate"))
                                .setTotalAmount((BigDecimal) getMap(actualList.stream().filter(a -> a.getDueBillNo().equals(assetAmount.getDueBillNo())).collect(Collectors.toList())).get("totalAmount"))
                                .setStatus((String) getMap(actualList.stream().filter(a -> a.getDueBillNo().equals(assetAmount.getDueBillNo())).collect(Collectors.toList())).get("status_"));
                        break;
                    }
                }
            }
            csvDao.addActualList(actualList);
            csvDao.addAssetList(assetList);
            msg = "保存成功!";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存失败!" + e.getMessage();
        }
        log.info("msg:{}", msg);
        return msg;
    }
    private Map<String, Object> getMap(List<ActualAmount> actualList) throws Exception {
        Map<String, Object> map = new HashMap<>();
        LocalDate maxBatchDate = actualList.stream().max((a, b) -> a.getBatchDate().compareTo(b.getBatchDate())).get().getBatchDate();
        BigDecimal totalAmount = actualList.stream().map(ActualAmount::getTermAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        String status_ = "";
        for (ActualAmount actualAmount : actualList) {
            String status = actualAmount.getStatus();
            if (!status.equals("结清")) {
                status_ = "逾期";
            } else {
                status_ = "结清";
            }
        }
        map.put("maxBatchDate", maxBatchDate);
        map.put("totalAmount", totalAmount);
        map.put("status_", status_);
        log.info("map:{}", JSON.toJSONString(map));
        return map;
    }
    @RequestMapping("/check")
    public DataCheckResult check(){
        List<Map<String,Object>> list=csvDao.check();
        log.info("list:{}",JSON.toJSONString(list));
        return DataCheckResult.ok("校验实际还款和资产信息表", list.size());
    }
    @RequestMapping("/check2")
    public DataCheckResult check2(){
        Map<String, Object> map = csvDao.check2();
        log.info("list:{}",JSON.toJSONString(map));
        BigDecimal term = (BigDecimal) map.get("SUM(m.term_amount)");
        BigDecimal total = (BigDecimal) map.get("SUM(n.total_amount)");
        return term.compareTo(total)==0?DataCheckResult.ok("校验实际还款和资产信息表",0 )
                :DataCheckResult.ok("校验实际还款和资产信息表", 1);
    }
}
