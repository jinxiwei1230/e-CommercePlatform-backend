package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类别销量统计DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySalesStatsDTO {
    private Long categoryId;       // 类别ID
    private String categoryName;   // 类别名称
    private Integer totalSales;    // 总销量
    private Double totalAmount;    // 销售总额
    private Integer productCount;  // 商品数量
} 