package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户季度消费统计DTO
 */
@Data
public class UserQuarterlyConsumptionDTO {
    private Map<String, Map<String, BigDecimal>> yearlyData;  // 年份 -> 季度 -> 金额
} 