package com.hmdp.config;

import com.hmdp.dto.Result;
import com.hmdp.utils.limit.LimitException;
import com.sun.xml.internal.ws.handler.HandlerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        if (e instanceof HandlerException){
            return handleLimitException((LimitException) e);
        }
        log.error(e.toString(), e);
        return Result.fail("服务器异常");
    }
    public Result handleLimitException(LimitException e){
        log.error(e.toString(),e);
        return Result.fail(e.getMessage());
    }
}
