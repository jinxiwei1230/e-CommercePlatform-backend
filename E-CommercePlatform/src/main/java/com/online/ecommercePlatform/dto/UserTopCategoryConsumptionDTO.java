package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 用户顶级类别消费DTO
 */
@Data
public class UserTopCategoryConsumptionDTO {
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
} 