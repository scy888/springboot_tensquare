package com.tensquare.batch.batch;

import com.tensquare.batch.pojo.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 11:31:21
 * @describe:
 */
@Component
public class StudentProcessor implements ItemProcessor<Student,Student> {
    @Override
    public Student process(Student student) throws Exception {

        return student;
    }
}
