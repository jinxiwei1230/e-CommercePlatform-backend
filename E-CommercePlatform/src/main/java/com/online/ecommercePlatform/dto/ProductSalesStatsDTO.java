package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品销量统计DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesStatsDTO {
    private Long productId;      // 商品ID
    private String productName;  // 商品名称
    private String imageUrl;     // 商品图片URL
    private Integer sales;       // 销量
    private Double price;        // 价格
    private Double totalAmount;  // 销售总额
    private Long categoryId;     // 分类ID
    private String categoryName; // 分类名称
} 