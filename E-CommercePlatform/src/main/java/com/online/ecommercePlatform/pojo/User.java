package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 用户基本信息实体类
 */
@Data
public class User {
    private Long userId; // 用户唯一标识（主键）
    private String username; // 用户名（需唯一）
    private String password; // 加密存储的用户密码
    private String email; // 绑定的邮箱（支持解绑）
    private String phone; // 绑定的手机号（支持解绑）
    private String gender; // 性别（男 / 女 / 保密）
    private Integer age; // 年龄
    private Boolean isVip; // 是否为 VIP 用户（管理员分配）
    private LocalDateTime createTime; // 注册时间
    private LocalDateTime updateTime; // 信息更新时间

}