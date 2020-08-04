package com.tensquare.batch.feginClient;

import com.tensquare.req.UserDtoReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.feginClient
 * @date: 2020-08-04 21:25:32
 * @describe:
 */
@FeignClient(name = "tensquare-test",serviceId ="userDtoFeignClient",url = "127.0.0.1:9023")
public interface UserDtoFeignClient {
    @RequestMapping("test/userDome/select2")
    public List<UserDtoReq> select2(@RequestBody UserDtoReq userDtoReq);
    @RequestMapping("test/userDome/updateUserDto/{name}/{age}")
    public List<UserDtoReq> updateUserDto(@PathVariable("name") String name, @PathVariable("age") int age);
}
