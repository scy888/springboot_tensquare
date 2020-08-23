package com.tensquare.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.result
 * @date: 2020-08-23 11:57:49
 * @describe:
 */
@Data
@Accessors(chain = true)
public class DataCheckResult {
    //校验数据的名称
    private String name;
    //错误的条数
    private int errorCount;
    //校验状态
    private boolean flag;
    //备注
    private String remark;

    public DataCheckResult(String name, int errorCount) {
        this.name = name;
        this.errorCount = errorCount;
        this.flag = errorCount==0;
        this.remark = flag ? "校验通过" : "校验不通过,异常数据:"+errorCount+"条" ;
    }
    public static DataCheckResult ok(String name,int errorCount){
        return new  DataCheckResult(name, errorCount);
    }
    public static DataCheckResult ok(String name,int errorCount,String remark){
        return new DataCheckResult(name, errorCount)
                .setRemark(remark);
    }
}
