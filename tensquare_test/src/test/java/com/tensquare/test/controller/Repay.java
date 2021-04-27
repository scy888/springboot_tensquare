package com.tensquare.test.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author: scyang
 * @program: springboot_tensquare
 * @package: com.tensquare.test.controller
 * @date: 2021-04-15 00:00:46
 * @describe:
 */

//@Value
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Repay {
    private String dueBillNo;
    private BigDecimal amount;
    private Integer count;
    private LocalDate batchDate;

    public static void main(String[] args) {
        System.out.println(888);
    }
}
