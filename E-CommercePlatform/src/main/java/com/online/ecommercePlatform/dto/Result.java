package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 统一响应结果类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    private Integer code; // 响应码
    private String message; // 提示信息
    private T data; // 返回的数据
    
    // 成功状态码
    public static final int SUCCESS = 200;
    
    // 通用错误码
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int TOKEN_EXPIRED = 402;
    public static final int NOT_FOUND = 404;
    public static final int FORBIDDEN = 403;
    
    // 业务错误码
    public static final int EMAIL_PHONE_REQUIRED = 500;
    public static final int VERIFY_CODE_ERROR = 501;
    public static final int USERNAME_EXISTS = 502;
    public static final int EMAIL_EXISTS = 503;
    public static final int PHONE_EXISTS = 504;
    
    /**
     * 成功响应，无数据
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 成功响应，有数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    
    /**
     * 失败响应，使用BAD_REQUEST错误码
     */
    public static <T> Result<T> error(String errorMessage) {
        return build(BAD_REQUEST, "fail", null);
    }
    
    /**
     * 带错误码的失败响应
     */
    public static <T> Result<T> error(int code) {
        return build(code, "fail", null);
    }
    
    /**
     * 带错误码和自定义错误信息的失败响应
     */
    public static <T> Result<T> error(int code, String errorMessage) {
        return build(code, errorMessage, null);
    }
    
    /**
     * 自定义响应，支持自定义响应码、消息和数据
     */
    public static <T> Result<T> build(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
} 