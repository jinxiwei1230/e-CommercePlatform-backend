package com.online.ecommercePlatform.exception;

import com.online.ecommercePlatform.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *用于统一处理Controller层抛出的异常，返回规范的错误响应格式
 *   通过@RestControllerAdvice标记为全局异常处理类
 *   使用@ExceptionHandler捕获指定类型的异常
 *   将异常信息转换为统一的Result对象返回
 */
@RestControllerAdvice // 组合注解，相当于@ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获的Exception类型异常
     *
     * @param e 捕获到的异常对象
     * @return 统一错误响应对象Result
     *
     * @apiNote 1. 会打印异常堆栈到控制台（生产环境应考虑日志记录）
     *          2. 优先使用异常消息，若为空则返回默认提示
     *
     * @see Result 返回的错误结果格式
     */
    @ExceptionHandler(value = Exception.class) // 可处理所有Exception及其子类异常
    public Result handleException(Exception e) {
        // 打印异常堆栈（开发阶段方便调试）
        e.printStackTrace();

        // 返回错误结果：
        // 1. 优先使用异常自带的message
        // 2. 若message为空则返回默认提示
        return Result.error(
                StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败"
        );
    }
}