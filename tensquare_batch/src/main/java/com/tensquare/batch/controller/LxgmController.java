package com.tensquare.batch.controller;

import com.alibaba.fastjson.JSON;
import com.tensquare.batch.dao.LxgmDao;
import com.tensquare.batch.dao.LxgmPlanDao;
import com.tensquare.batch.dao.StudentJpaDao;
import com.tensquare.batch.feginClient.LxgmCsvFeignClient;
import com.tensquare.batch.pojo.RepaymentPlan;
import com.tensquare.batch.pojo.Student;
import com.tensquare.req.LxgmRepaymentPlanReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import utils.IdWorker;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.controller
 * @date: 2020-08-12 21:45:17
 * @describe:　乐信国民
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
    @Autowired
    private StudentJpaDao studentJpaDao;

    @RequestMapping("/saveList")
    public void saveList(@RequestBody List<RepaymentPlan> repaymentPlans) {
        log.info("repaymentPlans:{}", JSON.toJSONString(repaymentPlans));
        List<RepaymentPlan> exsitList = lxgmDao.findDueBillNosAndTerms(repaymentPlans);
        //List<RepaymentPlan> exsitList= lxgmPlanDao.findDueBillNosAndTerms(repaymentPlans);
        log.info("根据dueBillNo和Term查询出来的exsitList的size():{}条", exsitList.size());
        List<RepaymentPlan> insertList = new ArrayList<>();
        List<RepaymentPlan> updateList = new ArrayList<>();
        Map<String, RepaymentPlan> map = exsitList.stream()
                .collect(Collectors.toMap(e -> e.getDueBillNo() + "_" + e.getTerm(),
                        Function.identity(), (a, b) -> a));
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
                log.error("睡眠5秒后更新失败：{}", e.getMessage());
            }
        }
    }

    /**
     * 远程调用
     */
    @RequestMapping("/addList")
    public void addList(@RequestBody List<LxgmRepaymentPlanReq> lxgmRepaymentPlanReqs) {
        try {
            log.info("batch服务调用test服务开始...");
            lxgmCsvFeignClient.saveRepaymentPlan(lxgmRepaymentPlanReqs);
            log.info("batch服务调用test服务结束...");
        } catch (Exception e) {
            log.info("batch服务调用test服务异常：{}", e.getMessage());
        }
    }

    @RequestMapping("/random/{num}")
    public List<String> randomDueBillNo(@PathVariable int num,
                                        @RequestParam(name = "isFlag", required = false, defaultValue = "true") String flag) {
        log.info("num:{},flag:{}", num, flag);
        List<String> dueBillNos = lxgmDao.selectAll(LocalDate.parse("2020-06-06"));
        List<String> returnList = new ArrayList<>();
        log.info("查询所有dueBillNos:{}", dueBillNos);
        if (Boolean.valueOf(flag)) {
            for (int i = 0; i < num; i++) {
                returnList.add(dueBillNos.get(new Random().nextInt(dueBillNos.size())));
            }
        }
        Set<String> set = new HashSet<>(returnList);
        returnList = new ArrayList<>(set);
        log.info("添加查询所有returnList的个数为:{},returnList:{}", num, returnList);
        return returnList;
    }

    @RequestMapping("/addStudent")
    public void addStudent(@RequestBody Student student){
        studentJpaDao.save(student);
    }
}
