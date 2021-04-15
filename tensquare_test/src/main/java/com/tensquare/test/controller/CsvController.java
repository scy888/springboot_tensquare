package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.result.DataCheckResult;
import com.tensquare.result.LxgmTermStatus;
import com.tensquare.result.Result;
import com.tensquare.result.Tuple3;
import com.tensquare.test.dao.CsvDao;
import com.tensquare.test.dao.PlantAmountDaoJpa;
import com.tensquare.test.dao.RealAmountDaoJpa;
import com.tensquare.test.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import utils.IdWorker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
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
    @Autowired
    private PlantAmountDaoJpa plantAmountDaoJpa;
    @Autowired
    private RealAmountDaoJpa realAmountDaoJpa;

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
        LocalDate maxBatchDate = actualList.stream().max(Comparator.comparing(ActualAmount::getBatchDate)).get().getBatchDate();
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
    public DataCheckResult check() {
        List<Map<String, Object>> list = csvDao.check();
        log.info("list:{}", JSON.toJSONString(list));
        return DataCheckResult.ok("校验实际还款和资产信息表", list.size());
    }

    @RequestMapping("/check2")
    public DataCheckResult check2() {
        Map<String, Object> map = csvDao.check2();
        log.info("list:{}", JSON.toJSONString(map));
        BigDecimal term = (BigDecimal) map.get("SUM(m.term_amount)");
        BigDecimal total = (BigDecimal) map.get("SUM(n.total_amount)");
        return term.compareTo(total) == 0 ? DataCheckResult.ok("校验实际还款和资产信息表", 0)
                : DataCheckResult.ok("校验实际还款和资产信息表", 1);
    }

    @RequestMapping("/addList2")
    public Result addList2(@RequestBody List<PlantAmount> plantAmountList) {
        /**
         * @Description: 批量添加还款计划信息(对应多条实际还款信息)
         * @methodName: addList2
         * @Param: [plantAmountList]
         * @return: com.tensquare.result.Result
         * @Author: scyang
         * @Date: 2020/9/20 20:24
         */
        log.info("plantAmountList:{}", JSON.toJSONString(plantAmountList));
        //根据借据号查找
        List<PlantAmount> dbPlantAmounts = plantAmountDaoJpa.findByDueBillNoIn(plantAmountList.stream()
                .map(PlantAmount::getDueBillNo)
                .collect(Collectors.toList()));
        Map<String, PlantAmount> plantAmountMap = dbPlantAmounts.stream().collect(Collectors.toMap(PlantAmount::getDueBillNo, Function.identity(), (a, b) -> b));

        for (PlantAmount plantAmount : plantAmountList) {

            Map<String, Object> setMap = getParmsMap(plantAmount.getRealAmounts(), plantAmount.getDueBillNo());
            plantAmount.setTotalAmount((BigDecimal) setMap.get("amountSun"))
                    .setBatchDate((LocalDate) setMap.get("maxBatchDate"))
                    .setTermCount(String.valueOf(setMap.get("termCount")));

            for (RealAmount realAmount : plantAmount.getRealAmounts()) {
                realAmount.setDueBillNo(plantAmount.getDueBillNo());
                //保存之前根据借据号和期次查询
                RealAmount dbRealAmount = realAmountDaoJpa.findByDueBillNoAndTerm(realAmount.getDueBillNo(), realAmount.getTerm());
                if (dbRealAmount == null) {
                    //新增
                    realAmountDaoJpa.save(realAmount);
                    log.info("realAmount走新增逻辑...");
                } else {
                    //更新
                    realAmountDaoJpa.save(realAmount.setId(dbRealAmount.getId()));
                    log.info("realAmount走更新逻辑...");

                }
            }
            PlantAmount dbPlantAmount = plantAmountMap.get(plantAmount.getDueBillNo());
            if (dbPlantAmount == null) {
                //新增
                plantAmountDaoJpa.save(plantAmount);
                log.info("plantAmount走新增逻辑...");
            } else {
                //更新
                plantAmountDaoJpa.save(plantAmount.setId(dbPlantAmount.getId()));
                log.info("plantAmount走更新逻辑...");

            }
        }
        return Result.ok();
    }

    private Map<String, Object> getParmsMap(List<RealAmount> realAmounts, String dueBillNo) {
        /**
         * @Description: set参数的值
         * @methodName: getParmsMap
         * @Param: [realAmounts, dueBillNo]
         * @return: java.util.Map<java.lang.String , java.lang.Object>
         * @Author: scyang
         * @Date: 2020/9/20 21:10
         */
        Map<String, Object> returnMap = new HashMap<>();
        BigDecimal amountSun = realAmounts.stream().map(RealAmount::getTermAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        RealAmount realAmount = realAmounts.stream().max((a, b) -> a.getBatchDate().compareTo(b.getBatchDate())).orElseGet(() -> {
            return new RealAmount()
                    .setBatchDate(LocalDate.parse("2020-12-31"))
                    .setTerm(10);
        });
        returnMap.put("amountSun", amountSun);
        returnMap.put("maxBatchDate", realAmount.getBatchDate());
        returnMap.put("termCount", realAmount.getTerm());
        log.info("对应的借据号:{},还款的总费用:{},最后的还款日:{},总期数：{}", dueBillNo, amountSun, realAmount.getBatchDate(), realAmount.getTerm());
        return returnMap;
    }

    @RequestMapping("/getRepaymentPlan/{projectNo}")
    public Result<List<Tuple3<String, BigDecimal, Integer>>> getRepaymentPlan(@PathVariable String projectNo) {
        return Result.ok(csvDao.getRepaymentPlan(projectNo));
    }

    @RequestMapping(value = "/getRepaymentPlanList", method = RequestMethod.GET)
    public Result<List<LxgmRepaymentPlan>> getRepaymentPlanList(@RequestParam("projectNo") String projectNo, @RequestParam("dueBillNos") String dueBillNoList) {
        return Result.ok(csvDao.getgetRepaymentPlanList(projectNo, Arrays.asList(dueBillNoList.split(","))));
    }

    @RequestMapping(value = "/getRepaymentPlanList2", method = RequestMethod.GET)
    public Result<List<Tuple3<String, String, String>>> getRepaymentPlanList2(@RequestParam("projectNo") String projectNo, @RequestParam("dueBillNos") String dueBillNoList) {
        return Result.ok(csvDao.getgetRepaymentPlanList2(projectNo, Arrays.asList(dueBillNoList.split(","))));
    }
}
