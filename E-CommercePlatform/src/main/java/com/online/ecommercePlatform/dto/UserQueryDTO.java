package com.online.ecommercePlatform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询参数DTO
 */
@Data
@NoArgsConstructor
public class UserQueryDTO {
    private Integer page;         // 当前页码
    private Integer pageSize;     // 每页数量
    private String username;      // 用户名模糊搜索
    private Boolean isVip;        // VIP状态筛选
    private String role;          // 用户角色筛选
    private String sortBy;        // 排序字段
    private String sortOrder;     // 排序顺序（asc 或 desc）
} 