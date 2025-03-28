package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志信息实体类
 */
@Data
public class OperationLog {
    private Long logId; // 日志唯一标识（主键）
    private Long userId; // 用户 ID（外键关联用户表）
    private String action; // 操作类型（密码修改 / 删除评价）
    private LocalDateTime createTime; // 操作时间
}
