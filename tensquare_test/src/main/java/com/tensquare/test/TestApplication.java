package com.tensquare.test;

import common.DateUtils;
import common.JacksonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.IdWorker;
import utils.JwtUtil;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test
 * @date: 2020-07-04 11:12:04
 * @describe:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableJpaAuditing//自动加载时间的
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
    @Bean(name = "encoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
    @Bean
    public DateUtils dateUtils(){
        return new DateUtils();
    }
    @Bean
    public JacksonUtils jacksonUtils(){
        return new JacksonUtils();
    }
}
