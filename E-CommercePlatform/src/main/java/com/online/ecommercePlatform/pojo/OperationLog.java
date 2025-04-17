package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
public class OperationLog {
    private Long logId;           // 日志唯一标识（主键）
    private Long userId;          // 用户ID
    private String action;        // 操作类型
    private String targetTable;   // 操作对象的表名
    private Long targetId;        // 操作对象的ID
    private String description;   // 操作描述
    private String result;        // 操作结果（成功/失败）
    private LocalDateTime createTime; // 操作时间
}
