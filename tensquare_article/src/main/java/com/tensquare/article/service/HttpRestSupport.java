package com.tensquare.article.service;

import com.tensquare.article.config.HttpConfig;
import com.tensquare.article.jiekou.HttpRestOperations;
import common.ClaimpptException;
import common.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-03-19 21:32:13
 * @describe:
 */
public class HttpRestSupport implements HttpRestOperations {
    private static final Logger logger= LoggerFactory.getLogger(HttpRestSupport.class);
    private RestTemplate restTemplate;
    private HttpConfig httpConfig;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    private String getRequestUrl(String requestMappingUrl){
        /**
         * @Description: 拼接请求完整的url
         * @methodName: getRequestUrl
         * @Param: [requestMappingUrl]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2020/3/19 21:36
         */
        return httpConfig.getHostUrl()+requestMappingUrl;
    }

   public <T> T exchangeMap(String url, Map<String,Object> otherParam,Class<T> responseType){
       HttpHeaders httpHeaders=new HttpHeaders();
       httpHeaders.setContentType(new MediaType("application","json", Charset.forName("utf-8")));
       httpHeaders.setAccept(Arrays.asList(new MediaType[]{new MediaType("application","json", Charset.forName("utf-8"))}));
       HttpEntity<MultiValueMap<String,Object>> httpEntity=new HttpEntity<>(null,httpHeaders);
       UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getRequestUrl(url));
       Iterator<Map.Entry<String, Object>> iterator = otherParam.entrySet().iterator();
       while (iterator.hasNext()){
           builder.queryParam(iterator.next().getKey(),iterator.next().getValue());
       }
       ResponseEntity<String> responseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, httpEntity, String.class);
       return JacksonUtils.getInstance().readValue(responseEntity.getBody(), responseType);
   }

    private <T> T exchange(String url, HttpMethod method, Object requestObject, Class<T> responseType, Object... urlVariables) throws ClaimpptException {
        try {
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.setContentType(new MediaType("application","json",Charset.forName("utf-8") ));
            httpHeaders.setAccept(Arrays.asList(new MediaType[]{new MediaType("application","json",Charset.forName("utf-8"))}));
            HttpEntity<Object> httpEntity=new HttpEntity(requestObject,httpHeaders);
            url=getRequestUrl(url);
            logger.info("url{}:"+url);
            ResponseEntity<T> exchange = this.restTemplate.exchange(url, method, httpEntity, responseType, urlVariables);
            return exchange.getBody();
        } catch (RestClientException e) {
           throw new ClaimpptException(e.getMessage());
        }
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws ClaimpptException {
        try {
            T obj=this.exchange(url, HttpMethod.GET,null,responseType,urlVariables);
            return obj;
        } catch (Exception e) {
            throw new ClaimpptException(e.getMessage());
        }
    }

    @Override
    public <T> T postForObject(String url, Object requestObject, Class<T> responseType, Object... urlVariables) throws ClaimpptException {

        try {
            T obj=this.exchange(url, HttpMethod.POST,requestObject,responseType,urlVariables);
            return obj;
        } catch (Exception e) {
            throw new ClaimpptException(e.getMessage());
        }
    }
}
