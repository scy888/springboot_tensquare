package com.tensquare.result;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String msg;
    private Boolean success;
    private T data;

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(ErrorCodeEnum.S0000.name());
        result.setMsg(ErrorCodeEnum.S0000.getMessage());
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(ErrorCodeEnum.S0000.name());
        result.setMsg(ErrorCodeEnum.S0000.getMessage());
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ErrorCodeEnum.S0000.name());
        result.setMsg(msg);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.setCode(ErrorCodeEnum.B0103.name());
        result.setMsg(ErrorCodeEnum.B0103.getMessage());
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> error(String code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg("failed");
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(false);
        return result;
    }
}
