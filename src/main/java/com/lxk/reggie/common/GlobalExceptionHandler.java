package com.lxk.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        if (exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split(" ");
            return R.error(split[2] + "已存在");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception) {
        return R.error(exception.getMessage());
    }
}
