package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 销量统计查询参数DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatsQueryDTO {
    private Long categoryId;     // 分类ID
    private Date startDate;      // 开始日期
    private Date endDate;        // 结束日期
    private String sortBy;       // 排序字段(sales:销量, amount:销售额)
    private String sortOrder;    // 排序方式(asc,desc)
    private Integer pageNum;     // 页码
    private Integer pageSize;    // 每页数量
    
    // 分类ID是否为空，用于判断是否查询所有分类
    public boolean isAllCategories() {
        return categoryId == null || categoryId <= 0;
    }
    
    // 时间是否为空，用于判断是否按时间筛选
    public boolean hasTimeFilter() {
        return startDate != null && endDate != null;
    }
} 