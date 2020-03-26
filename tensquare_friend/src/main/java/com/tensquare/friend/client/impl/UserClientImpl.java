package com.tensquare.friend.client.impl;
import com.tensquare.friend.client.UserClient;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {
    @Override
    public void updateFollowCount(String userId, int num) {
        throw new RuntimeException("调用微服务失败,熔断器启动了.....");
    }

    @Override
    public void updateFansCount(String userId, int num) {
        throw new RuntimeException("调用微服务失败,熔断器启动了.....");
    }
}
