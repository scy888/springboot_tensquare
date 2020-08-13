package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.result.Result;
import com.tensquare.test.dao.CsvDao;
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

import java.util.ArrayList;
import java.util.List;
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
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
    @Autowired
    private CsvDao csvDao;
    @RequestMapping("/saveRepaymentPlan")
    Result saveRepaymentPlan(@RequestBody List<LxgmRepaymentPlanReq> lxgmRepaymentPlanReqs){
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

        List<LxgmRepaymentPlan> exsitList=csvDao.selectByDueBillNoAndTerm(dueBillNoTermVos);
        List<LxgmRepaymentPlan> insertList=new ArrayList<>();
        List<LxgmRepaymentPlan> updateList=new ArrayList<>();
        for (LxgmRepaymentPlan repaymentPlan : lxgmRepaymentPlans) {
            if (exsitList.stream().noneMatch(e->e.getDueBillNo().equals(repaymentPlan.getDueBillNo())&&
                    e.getTerm().equals(repaymentPlan.getTerm()))) {
                insertList.add(repaymentPlan);
            }
            else {
                updateList.add(repaymentPlan);
            }
        }
        log.info("insertList的size():{}条,updateList的size():{}条",insertList.size(),updateList.size());
        csvDao.insertList(insertList);
        csvDao.updateList(updateList);
        return Result.ok("success");
    }
}
