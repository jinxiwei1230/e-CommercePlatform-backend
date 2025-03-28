package com.online.ecommercePlatform.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类，提供日期格式化和解析功能
 */
public class DateUtils {

    // 定义标准的日期时间格式
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 LocalDateTime 对象格式化为字符串
     * @param dateTime 要格式化的 LocalDateTime 对象
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    /**
     * 将日期时间字符串解析为 LocalDateTime 对象
     * @param dateTimeStr 日期时间字符串
     * @return 解析后的 LocalDateTime 对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, FORMATTER);
    }
}
