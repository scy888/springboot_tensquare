package com.tensquare.test.dao;

import com.alibaba.fastjson.JSONObject;
import com.tensquare.test.pojo.User;
import com.tensquare.test.pojo.UserDto;
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
import java.util.ArrayList;
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

    public List<UserDto> selectUserByNameAndAge(List<UserDto> userDtoList) {
        String sql="select * from user_dto where (name,age) in(";
         String value="";
         List<Object> list=new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            value+="(?,?),\n";
            list.add(userDto.getName());
            list.add(userDto.getAge());
        }
        value=value.substring(0, value.lastIndexOf(","))+")";
        log.info("打印sql:{}",sql+value+";");
        Object[] args = list.toArray(new Object[list.size()]);
        return jdbcTemplate.query(sql+value+";", new BeanPropertyRowMapper<>(UserDto.class),args );
    }

    public int insertUserDtoList(List<UserDto> userDtoList){
        /** 批量添加 */
        log.info("userDtoList:{}", JSONObject.toJSONString(userDtoList));
        String sql="insert into user_dto(name,sex,age,create_date)values"+"\n";
        String values = userDtoList.stream().map(userDto -> {
            return "(?,?,?,?)";
        }).collect(Collectors.joining(",\n"));
        log.info("sql语句：{}",sql+values+";");
        List<Object> list=new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            list.add(userDto.getName());
            list.add(userDto.getSex());
            list.add(userDto.getAge());
            list.add(userDto.getCreateDate());
        }
        Object[] args = list.toArray(new Object[list.size()]);
       return jdbcTemplate.update(sql+values+";",args);
    }

    public int addUserList(List<UserDto> userDtoList){
        List<Object> list=new ArrayList<>();
        StringBuffer sb=new StringBuffer();
        sb.append("insert into user_dto(name,sex,age,create_date)values").append("\n");
        for (UserDto userDto : userDtoList) {
            sb.append("(?,?,?,?)").append(",").append("\n");
            list.add(userDto.getName());
            list.add(userDto.getSex());
            list.add(userDto.getAge());
            list.add(userDto.getCreateDate());
        }
        Object[] args = list.toArray(new Object[list.size()]);
        sb=sb.deleteCharAt(sb.lastIndexOf(",")).append(";");
        log.info("sql语句：{}",sb.toString());
        return jdbcTemplate.update(sb.toString(),args);
    }

    public int addList(List<UserDto> userDtoList){
        List<Object> list=new ArrayList<>();
        String sql="insert into user_dto(name,sex,age,context,create_date)values"+"\n";
        String value="";
        for (UserDto userDto : userDtoList) {
            value+="(?,?,?,?,?),"+"\n";
            list.add(userDto.getName());
            list.add(userDto.getSex());
            list.add(userDto.getAge());
            list.add(userDto.getContext());
            list.add(userDto.getCreateDate());
        }
        Object[] args = list.toArray(new Object[list.size()]);
        //value=value.substring(0, value.length()-2);
        value=value.substring(0,value.lastIndexOf(","));
        log.info("sql:{}",sql+value+";");
        return jdbcTemplate.update(sql+value+";",args);
    }


    public int saveOrUpadateUsers(List<UserDto> userDtoList) {
          int num=0;
        for (UserDto userDto : userDtoList) {
            String sql="insert into user_dto(age,context,create_date,name,sex)values(?,?,?,?,?)" +
                    "on duplicate key update context=?,create_date=?,sex=?";
            log.info("新增或修改sql：{}",sql);
            Object[] arge=new Object[]{userDto.getAge(),userDto.getContext(),userDto.getCreateDate(),
            userDto.getName(),userDto.getSex(),userDto.getContext(),userDto.getCreateDate(),userDto.getSex()};
            int update = jdbcTemplate.update(sql, arge);
            num=+update;
        }
        return num;
    }
}
