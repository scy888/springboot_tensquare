package com.tensquare.batch.batch;

import com.tensquare.batch.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.batch
 * @date: 2020-07-12 01:16:45
 * @describe:
 */
@Slf4j
@StepScope
@Component
public class StudentItemReader extends FlatFileItemReader<Student> {
  public StudentItemReader(@Value("${student.path}")String path, @Value("#{jobParameters[batchDate]}") String batchDate){
      log.info("路径参数，path:{}，时间参数，batchDate:{}",path,batchDate);
      Resource resource = new FileSystemResource(path+"/"+"tb_student"+".csv");
      Resource resource2 = new FileSystemResource("E:\\batch\\tb_student.csv");

      log.info("解析路径：{}",path+"/"+"tb_student"+".csv");
      this.setResource(resource2);
      this.setLinesToSkip(1);
      DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
      lineTokenizer.setNames(new String[]{"address", "age", "birthday", "createDate", "lastUpDate", "name", "sex"});
      lineTokenizer.setDelimiter(",");
      lineTokenizer.setStrict(false);

      BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
      fieldSetMapper.setTargetType(Student.class);

      DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
      defaultLineMapper.setLineTokenizer(lineTokenizer);
      defaultLineMapper.setFieldSetMapper(fieldSetMapper);
      this.setLineMapper(defaultLineMapper);
  }
}
