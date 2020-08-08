package com.tensquare.test.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-08-06 23:07:18
 * @describe:
 */
@Data
@Accessors(chain = true)
public class DueBillNoTermVo {
    private String dueBillNo;
    private Integer term;
}
