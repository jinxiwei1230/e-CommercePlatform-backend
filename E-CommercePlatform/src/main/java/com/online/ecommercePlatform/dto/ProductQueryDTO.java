package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 搜索商品的请求参数封装到 DTO 中
 */
@Data
public class ProductQueryDTO {
    private String categoryId;
    private Integer page = 1;
    private Integer pageSize = 20;
    private String sortBy;
    private String sortOrder;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // 判断是否查询所有商品
    public boolean isAllProducts() {
        return "all".equalsIgnoreCase(categoryId);
    }
} 