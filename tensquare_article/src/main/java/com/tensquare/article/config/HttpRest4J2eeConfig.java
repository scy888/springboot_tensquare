package com.tensquare.article.config;

import com.tensquare.article.jiekou.HttpRestOperations;
import com.tensquare.article.service.HttpRestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.config
 * @date: 2020-03-19 21:04:39
 * @describe:
 */
@Configuration
public class HttpRest4J2eeConfig {
    private static final Logger logger= LoggerFactory.getLogger(HttpRest4J2eeConfig.class);

   @Value("${claimppt_j2ee.http.hostUrl}")
    private String hostUrl;
   @Value("${request_rel_read_timeout}")
    private int readTimeout;
   @Value("${request_rel_connect_timeout}")
    private int connectTimeout;

   @Bean(name = "commonHttpRequest")
   public SimpleClientHttpRequestFactory getHttpRequest(){
       logger.info("create commonHttpRequest successfully");
       SimpleClientHttpRequestFactory requestFactory=new SimpleClientHttpRequestFactory();
       requestFactory.setReadTimeout(readTimeout);
       requestFactory.setConnectTimeout(connectTimeout);
       return requestFactory;
   }
   @Bean(name = "j2eeRestTemplate")
   public RestTemplate getRestTemplate(@Qualifier("commonHttpRequest")SimpleClientHttpRequestFactory httpRequest){

       return new RestTemplate(httpRequest);
   }
   @Bean(name = "j2eeHttpConfig")
   public HttpConfig getHttpConfig(){
       HttpConfig httpConfig=new HttpConfig();
       httpConfig.setHostUrl(hostUrl);
       httpConfig.setReadTimeout(readTimeout);
       httpConfig.setConnectTimeout(connectTimeout);
       return httpConfig;
   }
   @Bean(name = "httpRest4ClaimpptJ2ee")
   public HttpRestOperations getHttpRest4ClaimpptJ2ee(@Qualifier("j2eeHttpConfig")HttpConfig httpConfig,
                                                      @Qualifier("j2eeRestTemplate")RestTemplate restTemplate){
       HttpRestOperations httpRestOperations=new HttpRestSupport();
       ((HttpRestSupport) httpRestOperations).setHttpConfig(httpConfig);
       ((HttpRestSupport) httpRestOperations).setRestTemplate(restTemplate);
       return httpRestOperations;
   }
}
