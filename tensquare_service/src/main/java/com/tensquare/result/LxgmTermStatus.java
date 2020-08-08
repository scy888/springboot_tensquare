package com.tensquare.result;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-08-05 23:45:31
 * @describe:
 */
public enum LxgmTermStatus {

    N("正常"),
    O("逾期"),
    F("结清");

    private String desc;

    LxgmTermStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
