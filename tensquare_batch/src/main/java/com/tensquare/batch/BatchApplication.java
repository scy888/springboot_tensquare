package com.tensquare.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class);
    }
}
