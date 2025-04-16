package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 统一响应结果类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    private Integer code; // 响应码: 200-成功, 422-失败
    private String message; // 提示信息
    private T data; // 返回的数据
    
    /**
     * 成功响应，无数据
     */
    public static <T> Result<T> success() {
        return success("操作成功", null);
    }
    
    /**
     * 成功响应，有数据
     */
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }
    
    /**
     * 成功响应，自定义消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    /**
     * 失败响应，无数据
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(422);
        result.setMessage(message);
        return result;
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