package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 用户实体类
 */
@Data
public class User {
    private Long userId; // 用户ID
    private String username; // 用户名
    private String password; // 密码（加密存储）
    private String email; // 电子邮箱
    private String phone; // 手机号
    private String gender; // 性别 - 男/女/保密
    private String address; // 地址
    private Integer age; // 年龄
    private Boolean isVip; // 是否为VIP用户
    private String role = "普通用户"; // 用户角色，默认是普通用户
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}