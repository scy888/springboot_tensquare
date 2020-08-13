package com.tensquare.batch.dao;

import com.tensquare.batch.pojo.RepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.dao
 * @date: 2020-08-12 22:01:00
 * @describe:
 */
@Repository
@Slf4j
public class LxgmDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RepaymentPlan> findDueBillNosAndTerms(List<RepaymentPlan> repaymentPlans) {
        String query = repaymentPlans.stream().map(e -> {
            return "('" + e.getDueBillNo() + "'," + "'" + e.getTerm() + "')";
        }).collect(Collectors.joining(", "));
        String sql = "SELECT * FROM repayment_plan WHERE ( due_bill_no, term ) IN ( " + query + " )";
        //List<String> list = jdbcTemplate.queryForList(sql, String.class, "");
        //Integer count = jdbcTemplate.queryForObject(sql, Integer.class, "");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RepaymentPlan.class));
    }

    public int insertList(List<RepaymentPlan> insertList) {
        String sql = "INSERT INTO repayment_plan(project_no, partner_no,due_bill_no," +
                "term,term_status,batch_date, created_date, last_modified_date) VALUES " +
                "(?,?,?,?,?,?,?,?)";
        List<Object[]> list = insertList.stream().map(plan -> {
            Object[] objects = new Object[]{
                    plan.getProjectNo(),
                    plan.getPartnerNo(),
                    plan.getDueBillNo(),
                    plan.getTerm(),
                    plan.getTermStatus()==null ? null : plan.getTermStatus().name(),
                    plan.getBatchDate(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            };
            return objects;
        }).collect(Collectors.toList());

        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        log.info("insert sql语句:{}", sql);
        return Arrays.stream(ints).sum();
    }

    public int updateList(List<RepaymentPlan> updateList) {
        String sql = "update repayment_plan set project_no=?, partner_no=?,batch_date=?, term_status=?, last_modified_date=?" +
                " where (due_bill_no,term) in ((?,?))";
        List<Object[]> list = updateList.stream().map(e -> {
            return new Object[]{
                    e.getProjectNo(),
                    e.getPartnerNo(),
                    e.getBatchDate(),
                    e.getTermStatus()==null ? null : e.getTermStatus().name(),
                    LocalDateTime.now(),
                    e.getDueBillNo(),
                    e.getTerm()
            };
        }).collect(Collectors.toList());
        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        log.info("update sql语句:{}", sql);
        return Arrays.stream(ints).sum();
    }
}
