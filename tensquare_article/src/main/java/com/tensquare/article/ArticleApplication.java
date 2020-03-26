package com.tensquare.article;

import common.CaseReasonEnum;
import common.JwtUtils;
import common.Md5Utils;
import common.NumUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.IdWorker;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableEurekaClient
public class ArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}
	@Bean
	public NumUtils numUtils(){
		return new NumUtils();
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JwtUtils jwtUtils(){
		return new JwtUtils();
	}
	@Bean
	public Md5Utils md5Utils() {return new Md5Utils();}
	/*@Bean
	public PageHelper pageHelper(){
		PageHelper pageHelper=new PageHelper();
		Properties properties=new Properties();
		properties.getProperty("reasonable","true" );
		properties.setProperty("dialect","mysql");
		pageHelper.setProperties(properties);
		return pageHelper;
	}*/

	@Test
	public void test(){
		System.out.println(CaseReasonEnum.getEnumByStatusCode("CCB"));
		System.out.println(CaseReasonEnum.getEnumByNameDesc("踩踏事故"));
	}
}
