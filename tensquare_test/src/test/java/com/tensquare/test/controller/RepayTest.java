package com.tensquare.test.controller;

import com.tensquare.result.LxgmTermStatus;
import com.tensquare.result.Tuple3;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import common.JacksonUtils;
import common.ReflectUtils;
import common.StringUtils;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: springboot_tensquare
 * @package: com.tensquare.test.controller
 * @date: 2021-04-14 15:14:27
 * @describe:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RepayTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void test0() {
        String sql = "select id, project_no,due_bill_no,term,term_term_prin,term_status,batch_date from lxgm_repayment_plan ";
        // List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        // Map<String, Object> list = jdbcTemplate.queryForMap(sql);

        List<LxgmRepaymentPlan> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LxgmRepaymentPlan.class));
        log.info("list:{}", list);

        LocalDate batchDate = LocalDate.parse("2020-06-06");
        for (LxgmRepaymentPlan plan : list) {
            plan.setBatchDate(batchDate);
            batchDate = batchDate.plusDays(1);
        }

        System.out.println(list.stream().map(LxgmRepaymentPlan::getBatchDate).map(String::valueOf)
                .collect(Collectors.joining(",", "{", "}")));

        sql = "update lxgm_repayment_plan set batch_date=?,last_modified_date=? where due_bill_no=?";
        int[] ints = jdbcTemplate.batchUpdate(sql,
                list.stream().map(e -> new Object[]{e.getBatchDate(), LocalDateTime.now(), e.getDueBillNo()}).collect(Collectors.toList()));
        System.out.println(Arrays.stream(ints).sum());
        /*****************************************************************************************************/

    }

    @Test
    public void test01() {
        String sql = "select due_bill_no,batch_date,sum(term_term_prin),count(*) from lxgm_repayment_plan group by due_bill_no,batch_date order by batch_date";
        List<Repay> list = jdbcTemplate.query(sql, new RowMapper<Repay>() {
            @Override
            public Repay mapRow(ResultSet rs, int i) throws SQLException {
                return new Repay(rs.getString(1), rs.getBigDecimal(3), rs.getInt(4), rs.getObject(2, LocalDate.class));
            }
        });

        System.out.println(JacksonUtils.toString(list, true));

        System.out.println("****************************************************************************");

        sql = "select due_bill_no,batch_date,sum(term_term_prin) amount,count(*) count from lxgm_repayment_plan group by due_bill_no,batch_date order by batch_date";
        list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Repay.class));
        System.out.println(JacksonUtils.toString(list, true));
    }

    @Test
    public void test02() {
        String sql = "select due_bill_no,batch_date,sum(term_term_prin),count(*) from lxgm_repayment_plan where project_no= :projectNo and partner_no in (:partnerNos) group by due_bill_no,batch_date order by batch_date";
        Map<String, Object> map = new HashMap<>();
        map.put("projectNo", "WS0006200001");
        map.put("partnerNos", Arrays.asList("0006", "0007", "0008"));
        List<Repay> list = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Repay>() {
            @Override
            public Repay mapRow(ResultSet rs, int i) throws SQLException {
                return new Repay(rs.getString(1), rs.getBigDecimal(3), rs.getInt(4), rs.getObject(2, LocalDate.class));
            }
        });

        System.out.println(JacksonUtils.toString(list, true));

        System.out.println("****************************************************************************");

        sql = "select due_bill_no,batch_date,sum(term_term_prin) amount,count(*) count from lxgm_repayment_plan where project_no= :projectNo and partner_no in (:partnerNos) group by due_bill_no,batch_date order by batch_date";
        list = namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(Repay.class));
        System.out.println(JacksonUtils.toString(list, true));
    }

    @Test
    public void test03() {
        String sql = "select due_bill_no,batch_date,sum(term_term_prin),term_status from lxgm_repayment_plan where project_no= :projectNo and partner_no in (:partnerNos) group by due_bill_no,batch_date,term_status order by batch_date";
        Map<String, Object> map = new HashMap<>();
        map.put("projectNo", "WS0006200001");
        map.put("partnerNos", Arrays.asList("0006", "0007", "0008"));
        List<Tuple3<String, BigDecimal, LxgmTermStatus>> list = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Tuple3<String, BigDecimal, LxgmTermStatus>>() {
            @Override
            public Tuple3<String, BigDecimal, LxgmTermStatus> mapRow(ResultSet rs, int i) throws SQLException {
                return Tuple3.of(rs.getString(1), rs.getBigDecimal(3), LxgmTermStatus.valueOf(rs.getString(4)));
            }
        });
        System.out.println(JacksonUtils.toString(list, true));
    }

    @Test
    public void test04() throws Exception {
        List<LxgmRepaymentPlan> lxgmRepaymentPlans = Arrays.asList(new LxgmRepaymentPlan("WS0006200001", "0006", "1120060420501133914005", 2,
                        LocalDate.parse("2020-06-04"), LocalDate.parse("2020-06-05"), LocalDate.parse("2020-06-06"),
                        new BigDecimal("12"), new BigDecimal("0.36"), null, null, null, null, null, null, null, null, null, null, LxgmTermStatus.N, null, LocalDate.parse("2020-06-04"),
                        LocalDate.parse("2020-06-06"), LocalDateTime.now(), LocalDateTime.now()),
                new LxgmRepaymentPlan("WS0006200001", "0006", "1120060420501133914005", 3,
                        LocalDate.parse("2020-06-04"), LocalDate.parse("2020-06-05"), LocalDate.parse("2020-06-06"),
                        new BigDecimal("12"), new BigDecimal("0.36"), null, null, null, null, null, null, null, null, null, null, LxgmTermStatus.N, null, LocalDate.parse("2020-06-04"),
                        LocalDate.parse("2020-06-06"), LocalDateTime.now(), LocalDateTime.now()),
                new LxgmRepaymentPlan("WS0006200001", "0006", "1120060420501133914005", 4,
                        LocalDate.parse("2020-06-04"), LocalDate.parse("2020-06-05"), LocalDate.parse("2020-06-06"),
                        new BigDecimal("12"), new BigDecimal("0.36"), null, null, null, null, null, null, null, null, null, null, LxgmTermStatus.N, null, LocalDate.parse("2020-06-04"),
                        LocalDate.parse("2020-06-06"), LocalDateTime.now(), LocalDateTime.now()));


        String insertSql = StringUtils.getInsertSql("lxgm_repayment_plan", LxgmRepaymentPlan.class);

        List<Object[]> objectList = new ArrayList<>();
        for (LxgmRepaymentPlan plan : lxgmRepaymentPlans) {
            Object[] objects = StringUtils.getFieldValue(plan);
            objectList.add(objects);
            System.out.println(Arrays.asList(objects));
        }
        String sql = "delete from lxgm_repayment_plan where (due_bill_no,term) in ";
        String str = lxgmRepaymentPlans.stream().map(e -> "(" + e.getDueBillNo() + "," + e.getTerm() + ")").collect(Collectors.joining(",", "(", ")"));
        System.out.println(String.format("删除的sql语句:%s", sql + str));
        System.out.println(String.format("新增的sql语句:%s", insertSql));
        jdbcTemplate.batchUpdate(sql + str);
        jdbcTemplate.batchUpdate(insertSql, objectList);
    }


    //    this.termTermPenalty = termTermPenalty;
//        this.termTermFee = termTermFee;
//        this.termRepayPrin = termRepayPrin;
//        this.termRepayInt = termRepayInt;
//        this.termRepayPenalty = termRepayPenalty;
//        this.termRepayFee = termRepayFee;
//        this.termReducePrin = termReducePrin;
//        this.termReduceInt = termReduceInt;
//        this.termReducePenalty = termReducePenalty;
//        this.termReduceFee = termReduceFee;
    @Test
    public void test00() throws Exception {
        System.out.println(StringUtils.getInsertSql("lxgm_repayment_plan", LxgmRepaymentPlan.class, "id", "project_no", "effect_date", "last_modified_date", "created_date"));
        User user1 = new User("scy1", new Date(), 18, null, "应城1", "123_1", "123", BigDecimal.TEN, User.Status.F);
        User user2 = new User("scy2", new Date(), 19, null, "应城2", "123_2", "123", BigDecimal.TEN, User.Status.N);
        User user3 = new User("scy3", new Date(), 20, null, "应城3", "123_3", "123", BigDecimal.TEN, User.Status.O);
        List<User> userList = new ArrayList<>();
        Collections.addAll(userList, user1, user2, user3);
        StringBuffer sb = new StringBuffer();
        for (User user : userList) {
            Object[] objects = StringUtils.getFieldValue(user, "birthday", "sex");
            System.out.println(Arrays.asList(objects));
        }
        System.out.println(sb.toString());
        System.out.println(ReflectUtils.getFieldNames(LxgmRepaymentPlan.class));
    }
}
