package com.tensquare.test.dao;

import com.tensquare.test.pojo.DueBillNoTermVo;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utils.IdWorker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-08-06 23:20:55
 * @describe:
 */
@Repository
@Slf4j
public class CsvDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LxgmRepaymentPlan> selectByDueBillNoAndTerm2(List<DueBillNoTermVo> dueBillVos) {
        String query = dueBillVos.stream().map(e -> {
            return "('" + e.getDueBillNo() + "'," + "'" + e.getTerm() + "')";
        }).collect(Collectors.joining(", "));
        String sql = "SELECT * FROM lxgm_repayment_plan WHERE ( due_bill_no, term ) IN ( " + query + " )";
        //List<String> list = jdbcTemplate.queryForList(sql, String.class, "");
        //Integer count = jdbcTemplate.queryForObject(sql, Integer.class, "");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LxgmRepaymentPlan.class));
    }
    public List<LxgmRepaymentPlan> selectByDueBillNoAndTerm(List<DueBillNoTermVo> dueBillVos) {
        String query = dueBillVos.stream().map(e ->
                "("+ "?"+ "," + "?" + ")"
        ).collect(Collectors.joining(", "));
        String sql = "SELECT * FROM lxgm_repayment_plan WHERE ( due_bill_no, term ) IN ( " + query + " )";

       List<Object> list=new ArrayList<>();
//        for (DueBillNoTermVo dueBillVo : dueBillVos) {
//            list.add(dueBillVo.getDueBillNo());
//            list.add(dueBillVo.getTerm());
//        }
        dueBillVos.forEach(a->{
            list.add(a.getDueBillNo());
            list.add(a.getTerm());
        });
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LxgmRepaymentPlan.class),list.toArray(new Object[list.size()]));
    }

    public List<Integer> selectByDueBillNos(List<String> list){
        String sql="select term from lxgm_repayment_plan where due_bill_no in("+list.stream().map(a->{return "?";})
                .collect(Collectors.joining(","))+")";
        List<Object> objectList=new ArrayList<>();
        list.forEach(a->objectList.add(a));

        return jdbcTemplate.queryForList(sql, Integer.class, list.toArray(new Object[list.size()]));
    }
    public Integer insertList(List<LxgmRepaymentPlan> plans) {
        if (plans == null || plans.isEmpty()) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        String sql = "INSERT INTO lxgm_repayment_plan(project_no, partner_no, due_bill_no, term, term_start_date," +
                " term_due_date, term_grace_date, term_term_prin, term_term_int, term_term_penalty," +
                " term_term_fee, term_repay_prin, term_repay_int, term_repay_penalty, term_repay_fee, " +
                "term_reduce_prin, term_reduce_int, term_reduce_penalty, term_reduce_fee, term_status, " +
                "settle_date, effect_date, batch_date, created_date, last_modified_date) VALUES ";
        sb.append(sql).append("\n");
        sb.append("(?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?)");
        List<Object[]> list = plans.stream().map(plan -> {
            Object[] objects = new Object[]{
                    plan.getProjectNo(),
                    plan.getPartnerNo(),
                    plan.getDueBillNo(),
                    plan.getTerm(),
                    plan.getTermStartDate(),

                    plan.getTermDueDate(),
                    plan.getTermGraceDate(),
                    plan.getTermTermPrin(),
                    plan.getTermTermInt(),
                    plan.getTermTermPenalty(),

                    plan.getTermTermFee(),
                    plan.getTermRepayPrin(),
                    plan.getTermRepayInt(),
                    plan.getTermRepayPenalty(),
                    plan.getTermRepayFee(),

                    plan.getTermReducePrin(),
                    plan.getTermReduceInt(),
                    plan.getTermReducePenalty(),
                    plan.getTermReduceFee(),
                    plan.getTermStatus().name(),

                    plan.getSettleDate(),
                    plan.getEffectDate(),
                    plan.getBatchDate(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            };
            return objects;
        }).collect(Collectors.toList());

        int[] ints = jdbcTemplate.batchUpdate(sb.toString(), list);
        log.info("insert sql语句:{}",sb.toString());
        return Arrays.stream(ints).sum();
    }
    public Integer updateList(List<LxgmRepaymentPlan> updateList) {
        if (updateList == null || updateList.isEmpty()) {
            return 0;
        }
        String sql = "update lxgm_repayment_plan set term_start_date =?,term_due_date=?,term_grace_date=?,term_term_prin=?," +
                "term_term_int=?,term_term_penalty=?,term_term_fee=?,term_repay_prin=?," +
                "term_repay_int=?,term_repay_penalty=?,term_repay_fee=?,term_reduce_prin=?," +
                "term_reduce_int=?,term_reduce_penalty=?,term_reduce_fee=?,term_status=?," +
                "settle_date=?,effect_date=?,batch_date=?,last_modified_date=?" +
                "where due_bill_no = ? and term = ?";
        List<Object[]> list = updateList.stream().map(
                lxgmRepaymentPlan -> new Object[]{
                        lxgmRepaymentPlan.getTermStartDate(), lxgmRepaymentPlan.getTermDueDate(), lxgmRepaymentPlan.getTermGraceDate(), lxgmRepaymentPlan.getTermTermPrin(),
                        lxgmRepaymentPlan.getTermTermInt(), lxgmRepaymentPlan.getTermTermPenalty(), lxgmRepaymentPlan.getTermTermFee(), lxgmRepaymentPlan.getTermRepayPrin(),
                        lxgmRepaymentPlan.getTermRepayInt(), lxgmRepaymentPlan.getTermRepayPenalty(), lxgmRepaymentPlan.getTermRepayFee(), lxgmRepaymentPlan.getTermReducePrin(),
                        lxgmRepaymentPlan.getTermReduceInt(), lxgmRepaymentPlan.getTermReducePenalty(), lxgmRepaymentPlan.getTermReduceFee(), lxgmRepaymentPlan.getTermStatus().name(),
                        lxgmRepaymentPlan.getSettleDate(), lxgmRepaymentPlan.getEffectDate(), lxgmRepaymentPlan.getBatchDate(),
                        LocalDateTime.now(), lxgmRepaymentPlan.getDueBillNo(), lxgmRepaymentPlan.getTerm()
                }
        ).collect(Collectors.toList());

        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        log.info("update sql语句:{}",sql);

        return Arrays.stream(ints).sum();
    }
}
