package com.online.ecommercePlatform.exception;

import com.online.ecommercePlatform.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Result.build(422, e.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<?>> handleRuntimeException(RuntimeException e) {
        if (e.getMessage().contains("用户ID") && e.getMessage().contains("不存在")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.build(404, e.getMessage(), null));
        }
        if (e.getMessage().contains("已存在") || e.getMessage().contains("旧密码错误")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Result.build(422, e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.build(500, "服务器错误", null));
    }
}