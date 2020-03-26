package com.tensquare.friend.client;

import com.tensquare.friend.client.impl.UserClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 调用用户的微服务,建立用户客户端接口
 */
@FeignClient(value = "tensquare-user",fallback = UserClientImpl.class)
@Component
public interface UserClient {
    /**
     * 考取被调用用户微服务的更改关注数的方法
     * @param num
     */
    @PostMapping("/user/followcount/{userId}/{num}")
    public void updateFollowCount(@PathVariable("userId") String userId,@PathVariable("num") int num);

    /**
     * 考取被调用用户微服务的更改粉丝数的方法
     * @param num
     */
    @PostMapping("/user/fanscount/{userId}/{num}")
    public void updateFansCount(@PathVariable("userId") String userId,@PathVariable("num") int num);
}
