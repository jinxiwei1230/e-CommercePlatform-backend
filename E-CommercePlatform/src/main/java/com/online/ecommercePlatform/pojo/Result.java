package com.online.ecommercePlatform.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果封装类
 * @param <T> 响应数据的类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;    // 业务状态码：200-成功 400/401/404等-失败
    private String message;  // 提示信息
    private T data;          // 响应数据

    // 常用状态码
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;

    /**
     * 快速返回操作成功的响应结果（带响应数据）
     * @param data 响应数据
     * @param <E> 响应数据类型
     * @return 封装好的响应结果
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(SUCCESS, "success", data);
    }

    /**
     * 快速返回操作成功的响应结果（不带响应数据）
     * @return 封装好的响应结果
     */
    public static Result success() {
        return new Result(SUCCESS, "success", null);
    }

    /**
     * 快速返回操作失败的响应结果
     * @param message 错误提示信息
     * @return 封装好的响应结果
     */
    public static Result error(String message) {
        return new Result(BAD_REQUEST, message, null);
    }
    
    /**
     * 带错误码的失败响应
     */
    public static <T> Result<T> error(int code) {
        return new Result<>(code, "fail", null);
    }
    
    /**
     * 带错误码和自定义错误信息的失败响应
     */
    public static <T> Result<T> error(int code, String errorMessage) {
        return new Result<>(code, errorMessage, null);
    }
}