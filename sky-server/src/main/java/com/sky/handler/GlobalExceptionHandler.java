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
     * @param e 异常对象
     * @return 返回Result格式的错误信息
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(BaseException e){
        log.error("异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 捕获数据库表中的键重复异常
     * @param e 异常对象
     * @return 返回Result格式的错误信息
     * */
    @ExceptionHandler
    public Result<Object> duplicateKeyExceptionHandler(SQLIntegrityConstraintViolationException e){
        String msg = e.getMessage();
        if (msg.contains("Duplicate")){
            String duplicateName = msg.split(" ")[2];
            duplicateName = duplicateName.replaceAll("'", "");
            log.error("字段 {} 已存在", duplicateName);
            return Result.error(duplicateName + MessageConstant.COLUMN_EXISTED);
        }
        else {
            log.error("SQL异常, 异常信息: {}", msg);
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }

}
