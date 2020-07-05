package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.pojo.User;
import common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private Config config;
    @Autowired
    private DateUtils dateUtils;
    private static final Logger logger = LoggerFactory.getLogger(UserDomeController.class);

    @PostMapping("/addList")
    public List<User> addList(@RequestBody List<User> userList) {
        LocalDate brithday = LocalDate.of(1991, 10, 16);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020 - 1900, 6 - 1, 30, 15, 17, 26));
        for (User user : userList) {
            user.setBirthday(brithday);
            brithday = brithday.plusYears(1)
                    .minusMonths(1)
                    .plusDays(2);
            user.setCreateDate(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.add(Calendar.MINUTE, -2);
            calendar.add(Calendar.SECOND, 2);
        }
        log.info("userList:{}", userList);
        logger.info("userList:{}", JSON.toJSONString(userList));
        return userDomeDao.saveAll(userList);
    }

    @PostMapping("/select")
    public Map<String, Object> select(@RequestBody String paramJson) {
        JSONObject jsonObject = JSON.parseObject(paramJson);
        ObjectMapper objectMapper = new ObjectMapper();

        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        User user = jsonObject.getJSONObject("user").toJavaObject(User.class);
        Example<User> example = Example.of(user);
        Sort sort = Sort.by(Sort.Direction.DESC, "birthday").and(Sort.by(Sort.Direction.ASC, "age"));
        // Sort.by(Sort.Order.by("birthday")).descending().and(Sort.by(Sort.Order.by("age"))).ascending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<User> userPage = userDomeDao.findAll(example, pageable);
        Map<String, Object> retunMap = new HashMap<>();
        retunMap.put("content", userPage.getContent());
        retunMap.put("totalElements", userPage.getTotalElements());
        retunMap.put("totalPages", userPage.getTotalPages());
        log.info("map:{}", retunMap);
        return retunMap;
    }

    @RequestMapping("/findOne")
    public User findOne() {
        User user = new User();
        user.setName("周芷若2");
        Example<? extends User> example = Example.of(user);
        Optional<? extends User> optionalUser = userDomeDao.findOne(example);
        optionalUser.ifPresent(e -> {
            e.setName("芷若");
            e.setAge(e.getAge() > 30 ? e.getAge() : e.getAge() + 1);
        });
        log.info("user:{}", JSON.toJSONString(optionalUser.get()));
        String dateStr = dateUtils.getDateStr(1991, 10, 16, dateUtils.getPATTERN_TWO());
       log.info("date:{}",dateStr);
        log.info("name:{},address:{},age:{}",config.getName(),config.getAddress(),config.getAge());
        return optionalUser.get();
    }

    @RequestMapping("/idList")
    public List<User> getUserList(@RequestBody List<Integer> idList) {
        log.info("idList:{}", JSON.toJSONString(idList));
        List<User> userList = userDomeDao.findByUserIdInOrderByAgeDesc(idList);
        log.info("cuserList{}:", JSON.toJSONString(userList));
        return userList;
    }
    @RequestMapping("/like/{name}")
    public List<User> getUserListLike(@PathVariable String name) throws Exception {
        name= URLDecoder.decode(name,"utf-8" );
        name = name + "%";
        List<User> userList = userDomeDao.findByNameLike(name);
        /** 根据年龄从大到小排序 */
        userList= userList.stream().distinct().sorted((user1,user2)->Integer.compare(user2.getAge(), user1.getAge())).collect(Collectors.toList());
        log.info("cuserList{}:", JSON.toJSONString(userList));
        return userList;
    }
    @GetMapping("/greaterThanEqual/{age}")
    public List<User> findByAgeGreaterThanEqualOrderByBirthday(@PathVariable int age){
        List<User> userList= userDomeDao.findByAgeGreaterThanEqualOrderByBirthdayAsc(age);
        /** 根据姓名和年龄排序 */
        Map<String, List<User>> listMap = userList.stream().distinct().collect(Collectors.groupingBy(user -> user.getName() + ":" + user.getAge()));
        log.info("listMap:{}"+listMap);
        return userList;
    }
    @GetMapping("/ageOrName/{age}/{name}")
    public List<User> ageOrNameUserList(@PathVariable int age,@PathVariable String name){
        List<User> userList =userDomeDao.findByAgeOrName(age,name);
        log.info("userList:{}"+userList);
        /** 根据姓名和年龄去重 */
        userList=  userList.stream().distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(user->user.getName()+":"+user.getAge()))),
                        ArrayList::new));
        log.info("userList:{}"+userList);
        return userList;
    }
    @RequestMapping("/update/{id}")
    public User update(@PathVariable int id){
        Optional<User> optional = userDomeDao.findById(id);
        if (optional.isPresent()) {
            log.info("修改前的user:{}",optional.get());
            log.info("name:{},address:{},age:{}",config.getName(),config.getAddress(),config.getAge());
            User user = optional.get();
            user = userDomeDao.save(user.setName(user.getName() + "2").setAddress(user.getAddress() + "2"));
            log.info("修改后的user:{}",user);
            return user;
        }
      /*  optional.ifPresent(user->{
            user.setName(user.getName()+"2");
            user.setAddress(user.getAddress()+"2");

        });*/
      return null;
    }
    @GetMapping("/updateBy/{id}/{address}")
    //@Transactional
    public void updateBy(@PathVariable int id,@PathVariable String address){
        userDomeDao.updateBy(id,address);
    }
    @GetMapping("/selectBy/{address}/{sex}")
    public List<User> selectBy(@PathVariable String address,@PathVariable String sex){
        List<User> userList= userDomeDao.selectBy(address,sex);
        log.info("userList:{}",JSON.toJSONString(userList));
        return userList;
    }
}
