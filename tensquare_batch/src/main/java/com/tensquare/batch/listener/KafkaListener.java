package com.tensquare.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.listener
 * @date: 2020-07-11 22:18:14
 * @describe:
 */
@Slf4j
@Component
public class KafkaListener {
    @org.springframework.kafka.annotation.KafkaListener(topics = "hello")
    public void hello(ConsumerRecord<String,Object> consumerRecord, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        Object value = consumerRecord.value();
        log.info("获得消息：{}",value);
        acknowledgment.acknowledge();
    }
}
