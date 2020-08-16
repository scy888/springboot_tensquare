package com.tensquare.batch.controller;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.dao.LxgmDao;
import com.tensquare.batch.dao.LxgmPlanDao;
import com.tensquare.batch.feginClient.LxgmCsvFeignClient;
import com.tensquare.batch.pojo.RepaymentPlan;
import com.tensquare.req.LxgmRepaymentPlanReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.controller
 * @date: 2020-08-12 21:45:17
 * @describe:
 */
@RestController
@Slf4j
public class LxgmController {
    @Autowired
    private LxgmDao lxgmDao;
    @Autowired
    private LxgmCsvFeignClient lxgmCsvFeignClient;
    @Autowired
    private LxgmPlanDao lxgmPlanDao;

    @RequestMapping("/saveList")
    public void saveList(@RequestBody List<RepaymentPlan> repaymentPlans) {
        log.info("repaymentPlans:{}", JSON.toJSONString(repaymentPlans));
        List<RepaymentPlan> exsitList = lxgmDao.findDueBillNosAndTerms(repaymentPlans);
        //List<RepaymentPlan> exsitList= lxgmPlanDao.findDueBillNosAndTerms(repaymentPlans);
        log.info("根据dueBillNo和Term查询出来的exsitList的size():{}条", exsitList.size());
        List<RepaymentPlan> insertList = new ArrayList<>();
        List<RepaymentPlan> updateList = new ArrayList<>();
        Map<String, RepaymentPlan> map = exsitList.stream()
                .collect(Collectors.toMap(e -> e.getDueBillNo() + "_" + e.getTerm(), Function.identity()));
        for (RepaymentPlan repaymentPlan : repaymentPlans) {
            if (map.get(repaymentPlan.getDueBillNo() + "_" + repaymentPlan.getTerm()) == null) {
                insertList.add(repaymentPlan);
            } else {
                updateList.add(repaymentPlan);
            }
        }
        log.info("insertList的size():{}条,updateList的size():{}条", insertList.size(), updateList.size());
        int count = 0;
        if (insertList != null && insertList.size() > 0) {
            count = lxgmDao.insertList(insertList);
            log.info("新增：{}条", count);
        }
        if (updateList != null && updateList.size() > 0) {
            count = lxgmDao.updateList(updateList);
            log.info("更新：{}条", count);
            try {
                Thread.sleep(5000);
                //lxgmPlanDao.updateList(updateList);
                log.info("睡眠5秒执行...");
            } catch (InterruptedException e) {
                log.error("睡眠5秒后更新失败：{}",e.getMessage());
            }
        }
    }
    /** 远程调用 */
    @RequestMapping("/addList")
    public void addList(@RequestBody List<LxgmRepaymentPlanReq> lxgmRepaymentPlanReqs){
        try {
            log.info("batch服务调用test服务开始...");
            lxgmCsvFeignClient.saveRepaymentPlan(lxgmRepaymentPlanReqs);
            log.info("batch服务调用test服务结束...");
        } catch (Exception e) {
            log.info("batch服务调用test服务异常：{}",e.getMessage());
        }
    }
}
