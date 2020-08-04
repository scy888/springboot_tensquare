package com.tensquare.friend.client.impl;
import com.tensquare.friend.client.User2Client;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements User2Client {
    @Override
    public void updateFollowCount(String userId, int num) {
        throw new RuntimeException("调用微服务失败,熔断器启动了.....");
    }

    @Override
    public void updateFansCount(String userId, int num) {
        throw new RuntimeException("调用微服务失败,熔断器启动了.....");
    }
}
