package com.tensquare.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.listener
 * @date: 2020-09-06 21:49:38
 * @describe:
 */
@Component
@Slf4j
public class ApplicationListener {
    @EventListener
    public void applicationListener(String jobName){

        CompletableFuture.runAsync(()->{
            log.info("监听器获取的jobName:{}",jobName);
        });
    }
}
