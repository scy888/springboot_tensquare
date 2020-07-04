package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.pojo.User;
import common.ResponeData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.controller
 * @date: 2020-07-04 16:12:43
 * @describe:
 */
@RestController
@RequestMapping("/userDome")
@Slf4j
public class UserDomeController {
    @Autowired
    private UserDomeDao userDomeDao;
    private static final Logger logger= LoggerFactory.getLogger(UserDomeController.class);

    @PostMapping("/addList")
    public List<User> addList(@RequestBody List<User> userList){
        LocalDate brithday = LocalDate.of(1991, 10,16 );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020-1900,6-1,30,15,17,26));
        for (User user : userList) {
            user.setBirthday(brithday);
            brithday= brithday.plusYears(1)
                    .minusMonths(1)
                    .plusDays(2);
            user.setCreateDate(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.add(Calendar.MINUTE, -2);
            calendar.add(Calendar.SECOND, 2);
        }
        log.info("userList:{}",userList);
        logger.info("userList:{}", JSON.toJSONString(userList));
       return userDomeDao.saveAll(userList);
    }
    @PostMapping("/select")
    public Map<String,Object> select(@RequestBody String paramJson){
        JSONObject jsonObject = JSON.parseObject(paramJson);
        ObjectMapper objectMapper=new ObjectMapper();

        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        User user = jsonObject.getJSONObject("user").toJavaObject(User.class);
        Example<User> example=Example.of(user);
        Sort sort=Sort.by(Sort.Direction.DESC,"birthday").and(Sort.by(Sort.Direction.ASC,"age"));
       // Sort.by(Sort.Order.by("birthday")).descending().and(Sort.by(Sort.Order.by("age"))).ascending();
        Pageable pageable= PageRequest.of(pageNum-1, pageSize, sort);
        Page<User> userPage = userDomeDao.findAll(example, pageable);
        Map<String,Object> retunMap=new HashMap<>();
        retunMap.put("content", userPage.getContent());
        retunMap.put("totalElements", userPage.getTotalElements());
        retunMap.put("totalPages", userPage.getTotalPages());
        log.info("map:{}",retunMap);
        return retunMap;
    }
    @RequestMapping("/findOne")
    public User findOne(){
       User user= new User();
        user.setName("周芷若2");
        Example<? extends User> example=Example.of(user);
        Optional<? extends User> optionalUser = userDomeDao.findOne(example);
        optionalUser.ifPresent(e->{
            e.setName("芷若");
            e.setAge(e.getAge()>30 ? e.getAge() : e.getAge()+1);
        });

        log.info("user:{}",JSON.toJSONString( optionalUser.get()));
        return optionalUser.get();
    }
}
