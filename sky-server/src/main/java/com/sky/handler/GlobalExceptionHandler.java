package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex 异常对象
     * @return 返回Result格式的错误信息
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获数据库表中的键重复异常
     * @param e 异常对象
     * @return 返回Result格式的错误信息
     * */
    @ExceptionHandler
    public Result<Object> duplicateKeyExceptionHandler(SQLIntegrityConstraintViolationException e){
        String msg = e.getMessage();
        String duplicateName = msg.split(" ")[2];
        duplicateName = duplicateName.replaceAll("'", "");
        log.error("{} 用户名已存在", duplicateName);
        return Result.error(duplicateName + MessageConstant.NAME_EXISTED);
    }
}
