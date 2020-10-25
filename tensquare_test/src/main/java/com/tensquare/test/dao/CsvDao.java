package com.tensquare.test.dao;

import com.alibaba.fastjson.JSON;
import com.tensquare.result.Tuple3;
import com.tensquare.test.pojo.ActualAmount;
import com.tensquare.test.pojo.AssetAmount;
import com.tensquare.test.pojo.DueBillNoTermVo;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
                "(" + "?" + "," + "?" + ")"
        ).collect(Collectors.joining(", "));
        String sql = "SELECT * FROM lxgm_repayment_plan WHERE ( due_bill_no, term ) IN ( " + query + " )";

        List<Object> list = new ArrayList<>();
//        for (DueBillNoTermVo dueBillVo : dueBillVos) {
//            list.add(dueBillVo.getDueBillNo());
//            list.add(dueBillVo.getTerm());
//        }
        dueBillVos.forEach(a -> {
            list.add(a.getDueBillNo());
            list.add(a.getTerm());
        });
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LxgmRepaymentPlan.class), list.toArray(new Object[list.size()]));
    }

    public List<Integer> selectByDueBillNos(List<String> list) {
        String sql = "select term from lxgm_repayment_plan where due_bill_no in(" + list.stream().map(a -> {
            return "?";
        })
                .collect(Collectors.joining(",")) + ")";
        List<Object> objectList = new ArrayList<>();
        list.forEach(a -> objectList.add(a));

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
        log.info("insert sql语句:{}", sb.toString());
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
        log.info("update sql语句:{}", sql);

        return Arrays.stream(ints).sum();
    }

    public void addActualList(List<ActualAmount> actualList) {
        String sql = "insert into actual_amount (due_bill_no,term,batch_date,term_amount,status)values(?,?,?,?,?)";
        List<Object[]> list = actualList.stream().map(actualAmount -> {
            Object[] objects = new Object[]{
                    actualAmount.getDueBillNo(),
                    actualAmount.getTerm(),
                    actualAmount.getBatchDate(),
                    actualAmount.getTermAmount(),
                    actualAmount.getStatus()
            };
            return objects;
        }).collect(Collectors.toList());
        jdbcTemplate.batchUpdate(sql, list);
    }

    public void addAssetList(List<AssetAmount> assetList) {
        String sql = "insert into asset_amount (due_bill_no,batch_date,total_amount,status)values(?,?,?,?)";
        List<Object[]> list = assetList.stream().map(assentAmount -> new Object[]{
                assentAmount.getDueBillNo(),
                assentAmount.getBatchDate(),
                assentAmount.getTotalAmount(),
                assentAmount.getStatus()
        }).collect(Collectors.toList());
        jdbcTemplate.batchUpdate(sql, list);

    }

    public List<Map<String, Object>> check() {
        String sql = "SELECT m.due_bill_no, m.term_amount, n.total_amount FROM \n" +
                "(SELECT due_bill_no, SUM(term_amount) term_amount FROM actual_amount WHERE due_bill_no IN (SELECT due_bill_no FROM asset_amount WHERE STATUS='结清')\n" +
                " GROUP BY due_bill_no ORDER BY due_bill_no) m,\n" +
                "(SELECT due_bill_no,SUM(total_amount) total_amount FROM asset_amount WHERE STATUS='结清' GROUP BY due_bill_no) n\n" +
                "WHERE m.due_bill_no=n.due_bill_no AND m.term_amount!=n.total_amount;";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> check2() {
        String sql = "SELECT SUM(m.term_amount),SUM(n.total_amount) FROM (SELECT SUM(term_amount) term_amount,due_bill_no FROM actual_amount WHERE due_bill_no IN(SELECT due_bill_no FROM asset_amount WHERE STATUS='结清')\n" +
                " GROUP BY due_bill_no) m ,(SELECT SUM(total_amount) total_amount,due_bill_no FROM asset_amount WHERE STATUS='结清' GROUP BY due_bill_no) n WHERE m.due_bill_no=n.due_bill_no;";
        return jdbcTemplate.queryForMap(sql);
    }

    public List<Tuple3<String, BigDecimal, Integer>> getRepaymentPlan(String projectNo) {
        String sql1 = "SELECT due_bill_no,IFNULL(SUM(IFNULL(term_term_prin,0)+IFNULL(term_term_int,0)),0) sum_term,COUNT(*) FROM " +
                "lxgm_repayment_plan WHERE project_no=? GROUP BY due_bill_no;";
        List<Tuple3<String, BigDecimal, Integer>> list1 = jdbcTemplate.query(sql1, new RowMapper<Tuple3<String, BigDecimal, Integer>>() {
            @Override
            public Tuple3<String, BigDecimal, Integer> mapRow(ResultSet rs, int i) throws SQLException {
                return new Tuple3<>(rs.getString(1), rs.getBigDecimal(2), rs.getInt(3));
            }
        }, projectNo);
        log.info("list1:{}", JSON.toJSONString(list1));
        BigDecimal sum = list1.stream().map(Tuple3::getSecond).reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("sum:{}", sum);
        String sql2 = "SELECT due_bill_no,term,IFNULL(SUM(IFNULL(term_term_prin,0)+IFNULL(term_term_int,0)),0) sum_term FROM \n" +
                " lxgm_repayment_plan WHERE project_no=:projectNo and due_bill_no in(:dueBillNos) GROUP BY due_bill_no,term;";
        Map<String,Object> map=new HashMap<>();
        map.put("projectNo", projectNo);
        map.put("dueBillNos", list1.stream().map(Tuple3::getFirst).collect(Collectors.toList()));
        List<Tuple3<String, Integer, BigDecimal>> list2 = namedParameterJdbcTemplate.query(sql2, map, new RowMapper<Tuple3<String, Integer, BigDecimal>>() {
            @Override
            public Tuple3<String, Integer, BigDecimal> mapRow(ResultSet rs, int i) throws SQLException {
                return new Tuple3<String, Integer, BigDecimal>(rs.getString(1),rs.getInt(2),rs.getBigDecimal(3));
            }
        });
        log.info("sql2:{}", sql2);
        log.info("list2:{}", JSON.toJSONString(list2));
        log.info("list2的长度:{}", list2.size());
        return list1;
    }
}
