package com.tensquare.batch.run;

import com.tensquare.batch.dao.BatchJobStatusJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.run
 * @date: 2020-09-06 21:38:21
 * @describe:
 */
@Component
@Slf4j
public class jobRun implements ApplicationRunner {
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private BatchJobStatusJpa batchJobStatusJpa;
    @Override
    public void run(ApplicationArguments args) throws Exception {
       log.info("batch初始化......");
    }
}
