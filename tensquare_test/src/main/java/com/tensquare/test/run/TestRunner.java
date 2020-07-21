package com.tensquare.test.run;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.run
 * @date: 2020-07-09 22:45:58
 * @describe:
 */
@Slf4j
@Component
//@Order(1)
public class TestRunner implements ApplicationRunner {
    private static final Logger logger= LoggerFactory.getLogger(ApplicationRunner.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String name="盛重阳";
        String sex="男";
        log.info("姓名:{},性别:{}",name,sex);
        logger.info("姓名:{},性别:{}",name,sex);
    }
}
