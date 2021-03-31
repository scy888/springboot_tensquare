package com.tensquare.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch
 * @date: 2020-07-11 20:57:28
 * @describe:
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableEurekaClient
@EnableJpaAuditing//自动加载时间的
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class BatchApplication {
    public static void main(String[] args) {
        long millis = System.currentTimeMillis();
        SpringApplication.run(BatchApplication.class);
        log.info("batch服务启动时间：{}毫秒",System.currentTimeMillis()-millis);
    }
}
