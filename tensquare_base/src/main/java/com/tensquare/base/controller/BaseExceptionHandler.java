package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerException(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
