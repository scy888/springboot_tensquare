package com.tensquare.test.provider;

import com.alibaba.fastjson.JSON;
import com.tensquare.client.UserClient;
import com.tensquare.req.UserDtoReq;
import com.tensquare.test.dao.UserDtoDaoJpa;
import com.tensquare.test.pojo.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.provider
 * @date: 2020-08-03 21:32:43
 * @describe:
 */

@Slf4j
@RestController
public class UserProvider implements UserClient {
    @Autowired
    private UserDtoDaoJpa userDtoDaoJpa;
    @Override
    public List<UserDtoReq> getUserDtos(@PathVariable("name")String name, @PathVariable("age")int age) {
        UserDtoReq userDtoReq=new UserDtoReq();
        userDtoReq.setAge(age).setName(name);
        List<UserDto> userDtoList = userDtoDaoJpa.findAll(Example.of(JSON.parseObject(JSON.toJSONString(userDtoReq), UserDto.class)));
        log.info("userDtoList:{}",JSON.toJSONString(userDtoList));
        List<UserDtoReq> userDtoReqList = userDtoList.stream().map(userDto -> {
            return JSON.parseObject(JSON.toJSONString(userDto), UserDtoReq.class);
        }).collect(Collectors.toList());
        log.info("userDtoReqList:{}",JSON.toJSONString(userDtoReqList));
        return userDtoReqList;
    }
}

