package com.tensquare.test.pojo;

import com.tensquare.test.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-19 17:07:17
 * @describe:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result  {
    private String resultCode;
    private String resultMessage;

    public static Result getResult(ExceptionEnum exceptionEnum){
        Result result=new Result();
        result.setResultCode(exceptionEnum.getRetCode());
        result.setResultMessage(exceptionEnum.getRetMessage());
        return result;
    };
}
