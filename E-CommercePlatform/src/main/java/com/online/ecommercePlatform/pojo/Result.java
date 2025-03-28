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
    private Integer code;    // 业务状态码：0-成功 1-失败
    private String message;  // 提示信息
    private T data;          // 响应数据

    /**
     * 快速返回操作成功的响应结果（带响应数据）
     * @param data 响应数据
     * @param <E> 响应数据类型
     * @return 封装好的响应结果
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 快速返回操作成功的响应结果（不带响应数据）
     * @return 封装好的响应结果
     */
    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    /**
     * 快速返回操作失败的响应结果
     * @param message 错误提示信息
     * @return 封装好的响应结果
     */
    public static Result error(String message) {
        return new Result(1, message, null);
    }
}