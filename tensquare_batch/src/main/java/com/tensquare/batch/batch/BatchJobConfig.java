package com.tensquare.batch.batch;

import com.tensquare.batch.config.BatchConfig;
import com.tensquare.batch.listener.JobListener;
import com.tensquare.batch.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 11:37:37
 * @describe:
 */
@Configuration
public class BatchJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BatchConfig batchConfig;
    @Autowired
    private JobListener jobListener;

    @Bean
    public Step studentStep1(FlatFileItemReader<Student> studentItemReader,
                             StudentProcessor studentProcessor,
                             StudentItemWrite studentItemWrite) {
        Step step = stepBuilderFactory.get("studentStep1")
                .<Student, Student>chunk(1)
                .reader(studentItemReader)
                .processor(studentProcessor)
                .writer(studentItemWrite)
                .taskExecutor(batchConfig.batchTaskExecutor())
                .throttleLimit(1)
                .allowStartIfComplete(true)
                .build();
        return step;
    }

    @Bean
    public Step studentStep2(SelectTask selectTask) {
        TaskletStep step = stepBuilderFactory.get("studentStep2")
                .tasklet(selectTask)
                .build();
        return step;
    }

    @Bean
    public Job StudentJob(Step studentStep1, Step studentStep2){
        Job job = jobBuilderFactory.get("studentJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .flow(studentStep1)
                .next(studentStep2)
                .end()
                .build();
        return job;
    }
}
