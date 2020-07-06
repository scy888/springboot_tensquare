package com.tensquare.test.pojo;

import lombok.Data;
import lombok.Getter;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-06 23:28:45
 * @describe:
 */
@Getter
public enum StatusMent {
    N(0,"正常"),
    M(1,"逾期"),
    F(2,"结清");

   private int code;
   private String desc;

    StatusMent(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
