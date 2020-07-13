package com.tensquare.batch.config;

import common.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.config
 * @date: 2020-07-13 23:29:49
 * @describe:
 */
@Slf4j
@Component
public class KafkaProvider {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, List<?> message) {
        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(topic, JacksonUtils.toString(message));
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("向Topic {} 发送消息成功：{}", topic, result.toString());
            }
            @Override
            public void onFailure(Throwable throwable) {
                log.error("向Topic {} 发送消息失败：{}", topic, throwable.getMessage());
            }
        });
    }
}
