package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;
import com.tensquare.result.LxgmTermStatus;
import com.tensquare.result.Tuple3;
import com.tensquare.test.pojo.LxgmRepaymentPlan;
import common.*;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
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
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${test.city}")
    private List<String> cityList;

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

    @Test
    public void test004() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("lxgm_repayment_plan.sql");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String str = new String(bytes, StandardCharsets.UTF_8);
        inputStream.close();
        System.out.println("批量执行sql前...");
        String[] sqls = str.split(";");
        System.out.println(Arrays.asList(sqls));
        String join = String.join(";", Arrays.asList(sqls));
        System.out.println("sql语句:\n" + join);
        jdbcTemplate.batchUpdate(sqls);
        System.out.println("批量执行sql后...");

        System.out.println("==============================================");

        //byte[] readAllBytes = Files.readAllBytes(Paths.get(this.getClass().getResource("/lxgm_repayment_plan.sql").toURI()));
        // jdbcTemplate.batchUpdate(new String(readAllBytes, StandardCharsets.UTF_8).split(";"));

        List<String> list = Files.readAllLines(Paths.get(this.getClass().getResource("/lxgm_repayment_plan.sql").toURI()));
        jdbcTemplate.batchUpdate(Arrays.stream(String.join(System.lineSeparator(), list)
                .split(System.lineSeparator())).filter(e -> !e.equals(System.lineSeparator()))
                .map(e -> e.substring(0, e.lastIndexOf(";")))
                .toArray(String[]::new));
    }

    @Test
    public void test005() {
        User user = getUser(true);
        user = Optional.ofNullable(user).map(e -> {
            User user_ = new User();
            BeanUtils.copyProperties(e, user_);
            user_.setAge(user_.getAge() + 1);
            return user_;
        }).orElseThrow(() -> new RuntimeException("该对象为空..."));
        System.out.println(String.format("打印的结果值:\n%s", JsonUtil.toJson(user, true)));
    }

    private User getUser(boolean flag) {
        if (flag) {
            return new User("zhangsan", new Date(), 18, "男", "湖北", "123", "123", BigDecimal.ONE, User.Status.F);
        } else {
            return null;
        }
    }

    @Test
    public void test06() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(this.getClass().getResource("/csv.json").toURI()));
        String jsonStr = String.join(System.lineSeparator(), lines);
        List<LxgmRepaymentPlan> plans = JSON.parseArray(jsonStr, LxgmRepaymentPlan.class);
        System.out.println(JSON.toJSONString(plans, true));
        System.out.println("======================================");

        Files.write(Paths.get("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources", "csv_.json"),
                jsonStr.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        Files.write(Paths.get("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources", "csv__.json"),
                lines, StandardOpenOption.CREATE);
        Files.write(Paths.get("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources", "csv___.json"),
                JSON.toJSONString(plans, true).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    @Test
    public void test006() throws Exception {
        List<User> userList = Arrays.asList(
                new User("赵敏", new Date(2020 - 1900, 7 - 1, 6), 20, "女", "蒙古", "123", "123", BigDecimal.ONE, User.Status.F),
                new User("周芷若", new Date(2020 - 1900, 8 - 1, 9), 19, "女", "峨嵋", "123", "123", BigDecimal.ONE, User.Status.F),
                new User("殷离", new Date(2020 - 1900, 6 - 1, 5), 18, "女", "灵蛇岛", "123", "123", BigDecimal.ONE, User.Status.F),
                new User("小昭", new Date(2020 - 1900, 5 - 1, 5), 17, "女", "波斯", "123", "123", BigDecimal.ONE, User.Status.F)
        );
        String jsonStr = JsonUtil.toJson(userList, true);
        System.out.println("JsonUtil.toJson(userList):\n" + jsonStr);
        Path path = Paths.get("/userFile", "jsonUser");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        Files.write(Paths.get(String.valueOf(path), "jsonStrList.json"), jsonStr.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        /***********************************************************/
        List<String> list = new ArrayList<>();
        for (User user : userList) {
            list.add(JsonUtil.toJson(user));
        }
        Files.write(Paths.get(String.valueOf(path), "jsonStr.json"), list.stream().collect(Collectors.joining(System.lineSeparator())).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        System.out.println(list.stream().collect(Collectors.joining(System.lineSeparator())));
        List<User> users = JsonUtil.fromJson(
                list.stream().collect(Collectors.joining("," + System.lineSeparator(), "[", "]")),
                new TypeReference<List<User>>() {
                }
        );
        System.out.println(JsonUtil.toJson(users, true));
        /***********************************************************************/
        list.clear();
        for (User user : userList) {
            list.add(JsonUtil.toJson(user, true));
        }
        Files.write(Paths.get(String.valueOf(path), "jsonStr_.json"),
                list.stream().collect(Collectors.joining("," + System.lineSeparator(), "[", "]")).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE);
        System.out.println("===================================================================");
        byte[] bytes = Files.readAllBytes(Paths.get(String.valueOf(path), "jsonStr_.json"));
        users = JsonUtil.fromJson(new String(bytes, StandardCharsets.UTF_8), new TypeReference<List<User>>() {
        });

        System.out.println("打印结果:\n" +
                "username birthday age sex address password mobile money status \n" +
                users.stream().map(e -> String.format("%1$2s %2$2s %3$2s %4$2s %5$2s %6$2s %7$2s %8$2s %9$2s",
                        e.getUsername(), e.getBirthday(), e.getAge(), e.getSex(), e.getAddress(), e.getPassword(), e.getMobile(), e.getMoney(), e.getStatus()))
                        .collect(Collectors.joining("\n"))
        );
    }

    @Test
    public void test007() {

        // mongoTemplate.dropCollection("user");
//        List<User> users = mongoTemplate.findAllAndRemove(Query.query(Criteria.where("id").in(
//                mongoTemplate.findAll(User.class).stream().map(User::getId).collect(Collectors.toList())
//        )), User.class);
        //mongoTemplate.insert(new User(1, "赵敏", new Date(2020 - 1900, 7 - 1, 6), 20, "女", "蒙古", "123", "123", BigDecimal.ONE, User.Status.F), "user");
        List<User> userList = Arrays.asList(
                new User(SnowFlake.getInstance().nextId(), "赵敏", new Date(2020 - 1900, 7 - 1, 6), 20, "女", "蒙古", "123", "123", BigDecimal.ONE, User.Status.F),
                new User(SnowFlake.getInstance().nextId(), "周芷若", new Date(2020 - 1900, 8 - 1, 9), 19, "女", "峨嵋", "123", "123", BigDecimal.ONE, User.Status.F),
                new User(SnowFlake.getInstance().nextId(), "殷离", new Date(2020 - 1900, 6 - 1, 5), 18, "女", "灵蛇岛", "123", "123", BigDecimal.ONE, User.Status.F),
                new User(SnowFlake.getInstance().nextId(), "小昭", new Date(2020 - 1900, 5 - 1, 5), 17, "女", "波斯", "123", "123", BigDecimal.ONE, User.Status.F)
        );

//        mongoTemplate.find(new Query(new Criteria().and("username")
//                        .in(userList.stream().map(User::getUsername).collect(Collectors.toList())))
//                , User.class).stream().map(User::getUsername).findFirst().orElseGet(() -> {
//            System.out.println("走新增逻辑...");
//            mongoTemplate.insert(userList, "user");
//            return null;
//        });


        for (User user : userList) {
            Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("username")
                    .is(user.getUsername())), User.class)).orElseGet(() -> {
                System.out.println("走新增逻辑...");
                mongoTemplate.insert(user, "user");
                return null;
            });
        }

        //mongoTemplate.upsert(Query.query(Criteria.where("username").is("赵敏")), Update.update("address", "元朝").inc("age", 1), User.class);//找不到就新增
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is("赵敏")),
                Update.update("address", "元朝").inc("age", 1), User.class);
        userList = mongoTemplate.find(Query.query(Criteria.where("username").regex(Pattern.compile("^.*" + "敏" + "*.$", Pattern.CASE_INSENSITIVE))), User.class, "user");
        System.out.println(userList);
    }

    @Test
    public void test008() throws Exception {
        //mongoTemplate.dropCollection(IncomeApply.class);
        if (!mongoTemplate.collectionExists(IncomeApply.class)) {
            mongoTemplate.createCollection(IncomeApply.class);
        }
        InputStream inputStream = this.getClass().getResourceAsStream("/income_appy.json");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        List<IncomeApply> incomeApplyList = JsonUtil.fromJson(jsonStr, new TypeReference<List<IncomeApply>>() {
        });
        System.out.println(String.format("incomeApplyList的长度:%d", incomeApplyList.size()));

        incomeApplyList = incomeApplyList.stream().peek(e -> e.setCreateTime(LocalDateTime.parse(e.getCreateTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        ).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(IncomeApply::getCreateTime)))
                        , ArrayList::new
                )
        );

        System.out.println(String.format("去重后的长度:%d", incomeApplyList.size()));

        for (IncomeApply incomeApply : incomeApplyList) {
            Optional.ofNullable(mongoTemplate.findById(incomeApply.getId(), IncomeApply.class)).orElseGet(() -> {
                incomeApply.setCreateTime(incomeApply.getCreateTime() + " 00:00:00");
                mongoTemplate.insert(incomeApply, "msg_log_info");
                return null;
            });
        }

        incomeApplyList = mongoTemplate.findAll(IncomeApply.class);
        Path path = Paths.get("E:\\ideaws\\springboot_tensquare\\tensquare_test\\src\\test\\resources", "income_apply.json");
        Files.write(path, JsonUtil.toJson(incomeApplyList, true).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        jsonStr = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        incomeApplyList = JsonUtil.fromJson(jsonStr, new TypeReference<List<IncomeApply>>() {
        });
        System.out.println(String.format("从mongodb查出的长度:%d", incomeApplyList.size()));

        incomeApplyList = mongoTemplate.find(Query.query(
                Criteria.where("projectNo").is("WS0010200001")
                        .and("EnterReqParam.service").is("incomeApply")
                        .and("createTime").gte("2021-01-01 00:00:00")
                        .lte("2021-02-16 00:00:00")
                        .and("partner").regex(Pattern.compile("^.*" + "1" + "*.$"))
                        .and("outReqParam.projectName").in("民生金租易鑫一期")
                ).with(PageRequest.of(3 - 1, 5, Sort.Direction.DESC, "createTime"))
                , IncomeApply.class);
        System.out.println(String.format("符合条件的长度:%d", incomeApplyList.size()));
        System.out.println(incomeApplyList.stream().map(IncomeApply::getCreateTime).collect(Collectors.joining(",")));
    /*
db.getCollection('msg_log_info').find(
    {
        "projectNo": "WS0010200001",
        "service": {
            $in: ["incomeApply", "loanApply", "loanApplyNotify", "loanApplyResult", "queryContract", "queryRepaymentPlan"]
        },
        "createTime": {
         $gte: "2020-12-01 00:00:00",
         $lte: "2020-12-30 00:00:00"
        },
      "enterReqParam.content": {
         $regex: "YX-202012181005039618045"
      }
    }
);  */
    }

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

        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostName = inetAddress.getHostName();
        String hostAddress = inetAddress.getHostAddress();
        System.out.println(String.format("主机名称:%s\n主机ip:%s", hostName, hostAddress));
        System.out.println(String.format("配置文件上获取的城市名:%s", cityList));
        System.out.println(String.format("配置文件上获取的城市名:%s", String.join(",", cityList)));
        System.out.println("========================================================");
        List<String> list = Arrays.asList("静夜思", "唐*李白", "窗前明月光", "疑似地上霜", "举头望明月", "低头思故乡");
        System.out.println(String.join(System.lineSeparator(), list));
        for (int i = 0; i < 10; i++) {
            System.out.println(SnowFlake.getInstance().nextId());
        }
    }
}
