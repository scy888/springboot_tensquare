package com.tensquare.test.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.controller
 * @date: 2020-07-05 16:10:18
 * @describe:
 */
@Data
@Configuration
public class Config {
    @Value("${person.name}")
    public   String name;
    @Value("${person.address}")
    private String address;
    @Value("${person.age}")
    private int age;
}
