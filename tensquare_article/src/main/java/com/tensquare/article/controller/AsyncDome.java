package com.tensquare.article.controller;

import common.DateUtil;
import common.ThreadUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare
 * @date: 2020-03-07 21:56:00
 * @describe: 异步任务
 */
@Component
public class AsyncDome {
    private static final Random random=new Random();
    @Async
    public void doTaskOne() throws Exception {
        System.out.println("开始做任务一");
        long start = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务一,耗时:"+(end-start)+"毫秒");
    }
    @Async
    public void doTaskTwo() throws Exception {
        System.out.println("开始做任务二");
        long start = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务二,耗时:"+(end-start)+"毫秒");
    }
    @Async
    public void doTaskThree() throws Exception {
        System.out.println("开始做任务三");
        long start = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务三,耗时:"+(end-start)+"毫秒");
    }
    @Scheduled(cron = "0 0 14 * * ?")
    public void doScheduled() throws Exception {
        ThreadUtils.getThread(new Runnable() {
            @Override
            public void run() {
                String dateToStr = DateUtil.DateToStr(new Date(), DateUtil.FORMATTWO);
                System.out.println(Thread.currentThread().getName()+" "+dateToStr);
            }
        });
    }
}
