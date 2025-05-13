package com.online.ecommercePlatform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户列表响应DTO
 */
@Data
@NoArgsConstructor
public class UserListDTO {
    private Long userId;          // 用户ID
    private String username;      // 用户名
    private Boolean isVip;        // 是否为VIP用户
    private String role;          // 用户角色
    private BigDecimal totalSpent;// 购物总金额
    private LocalDateTime createTime; // 创建时间
} 