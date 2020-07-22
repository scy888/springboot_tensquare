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
        List<User> userList = userDomeDaoJpa.findAll().subList(0,1 );
        log.info("userList:{}", JSONObject.toJSONString(userList));
        //userCache.put("userList",userList );
    }
}
