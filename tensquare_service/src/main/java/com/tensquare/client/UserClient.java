package com.tensquare.client;

import com.tensquare.req.UserDtoReq;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.client
 * @date: 2020-08-03 21:18:55
 * @describe:
 */

@RequestMapping("/client")
public interface UserClient {
    @RequestMapping("/userClient/{name}/{age}")
    public List<UserDtoReq> getUserDtos(@PathVariable("name")String name,@PathVariable("age")int age);
}
