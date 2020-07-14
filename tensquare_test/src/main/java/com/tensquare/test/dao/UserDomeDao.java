package com.tensquare.test.dao;

import com.tensquare.test.pojo.User;
import common.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.dao
 * @date: 2020-07-07 00:06:24
 * @describe:
 */
@Repository
@Slf4j
public class UserDomeDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public int saveOne(User user) {
        String sql="insert into tb_user_dto(address,age,birthday,create_date,name," +
                "sex,user_pay,status_ment,update_date)values" +
                "(?,?,?,?,?,?,?,?,?)";
        Object[] args=new Object[]{user.getAddress(),user.getAge(),user.getBirthday(),new Date(),
                                   user.getName(),user.getSex(),user.getUserPay(),user.getStatusMent().name(), LocalDateTime.now()};
        int update = jdbcTemplate.update(sql, args);
        return update;
    }

    public List<User> saveList(List<User> userList) {
        List<User> list = userList.stream().map(user -> {
            String sql = "insert into tb_user_dto(address,age,birthday,create_date,name," +
                    "sex,user_pay,status_ment,update_date)values" +
                    "(?,?,?,?,?,?,?,?,?)";
            Object[] args = new Object[]{user.getAddress(), user.getAge(), user.getBirthday(), new Date(),
                    user.getName(), user.getSex(), user.getUserPay(), user.getStatusMent().name(), LocalDateTime.now()};
            int update = jdbcTemplate.update(sql, args);
            return user;
        }).collect(Collectors.toList());
        return list;
    }

    public List<User> getList(String sex) {

        String sql="select * from tb_user_dto where sex=?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),sex);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, new Object[]{sex});
        log.info("mapList:{}", JacksonUtils.getInstance().toString(mapList));
        return userList;
    }

    public List<User> findUserByNameAndSex(List<User> userList) {
        String query = userList.stream().map(user -> {
            return "('" + user.getName() + "'," + "'" + user.getSex() + "')";
        }).collect(Collectors.joining(","));
        String sql="select * from tb_user_dto where (name,sex) in ("+query+")";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(User.class) );
    }
}
