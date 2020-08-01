package com.tensquare.test.run;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.tensquare.test.dao.UserDomeDaoJpa;
import com.tensquare.test.pojo.User;
import common.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.run
 * @date: 2020-07-22 00:04:18
 * @describe: 容器启动就加载
 */
@Component
@Slf4j
public class StartRun implements CommandLineRunner {
    @Autowired
    private UserDomeDaoJpa userDomeDaoJpa;
//    @CreateCache(name = "userCache",expire = 100,cacheType = CacheType.LOCAL)
//    private Cache<String,Object> userCache;
    @Override
    public void run(String... args) throws Exception {
        Object obj = new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                List<User> userList = userDomeDaoJpa.findAll().subList(0,1 );
                return userList;
            }
        }.call();
        log.info("多线程实现实现Callable接口,userList:{}", JSONObject.toJSONString(obj));

        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("开始实现多线程completableFuture....");
            return userDomeDaoJpa.findAll().subList(0, 1);
        }).exceptionally(ex -> {
            throw new RuntimeException("多线程调用completableFuture失败:{}",ex.getCause());
        });
        log.info("多线程实现实现CompletableFuture接口,userList:{}", JSONObject.toJSONString(completableFuture.get()));
    }
}
