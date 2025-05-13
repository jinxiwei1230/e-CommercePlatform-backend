package com.online.ecommercePlatform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员修改用户信息DTO
 */
@Data
@NoArgsConstructor
public class AdminUserUpdateDTO {
    private Long userId;          // 要修改的用户ID
    private String username;      // 用户名
    private String email;         // 邮箱
    private String phone;         // 手机号
    private String gender;        // 性别
    private Integer age;          // 年龄
    private Boolean isVip;        // 是否VIP用户
    private String role;          // 用户角色
} 