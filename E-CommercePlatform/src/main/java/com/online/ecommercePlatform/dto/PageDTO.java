package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 分页查询参数DTO
 */
@Data
public class PageDTO {
    private Integer page = 1;  // 当前页码，默认第1页
    private Integer pageSize = 10;  // 每页记录数，默认10条
} 