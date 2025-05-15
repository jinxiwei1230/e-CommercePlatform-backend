package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 顶级类别销量统计DTO
 */
@Data
public class TopCategorySalesStatsDTO {
    /**
     * 类别ID
     */
    private Long categoryId;
    
    /**
     * 类别名称
     */
    private String categoryName;
    
    /**
     * 总销量
     */
    private BigDecimal totalSales;
} 