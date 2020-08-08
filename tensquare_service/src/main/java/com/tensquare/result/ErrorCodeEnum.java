package com.tensquare.result;

/**
 * @description:
 * @program: sunLine
 * @author: jiancai.zhou
 * @date: 2020-04-28 16:31
 **/
public enum ErrorCodeEnum {
    S0000("成功"),

    // A表示用户端错误 B表示系统执行错误 C表示调用第三方错误
    A0101("请求内容为空"),
    A0102("请求内容校验失败"),
    A0199("请求异常"),

    // 系统错误
    B0101("请求超时"),
    B0102("网络异常"),
    B0103("未知异常"),

    // 逻辑错误
    B0201("客户信息不存在"),
    B0202("订单不存在"),
    B0203("借据不存在"),
    B0204("还款计划不存在"),
    B0205("重复请求覆盖"),
    B0206("重复请求忽略"),
    B0207("方法传递参数异常"),
    B0208("客户信息异常"),
    B0209("创建客户信息失败"),
    B0210("客户职业创建失败"),
    B0211("联系人信息创建失败"),
    B0212("地址信息创建失败"),
    B0213("用信申请创建失败"),
    B0214("客户三要素校验失败"),
    B0215("实还明细信息创建失败"),
    B0299("其它逻辑处理错误"),


    // 调用第三方错误
    C0101("调用超时")
    ;

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
