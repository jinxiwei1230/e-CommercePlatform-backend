package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.UserQuarterlyConsumptionDTO;
import com.online.ecommercePlatform.dto.UserOrderStatusDTO;
import com.online.ecommercePlatform.dto.UserTopCategoryConsumptionDTO;
import com.online.ecommercePlatform.pojo.Result;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 用户统计服务接口
 */
public interface UserStatsService {
    
    /**
     * 获取用户季度消费统计
     * @param userId 用户ID
     * @param year 年份（可选，默认当前年份）
     * @return 季度消费统计数据
     */
    Result<Map<String, Map<String, BigDecimal>>> getQuarterlyConsumption(Long userId, Integer year);

    /**
     * 获取用户订单状态统计
     * @param userId 用户ID
     * @return 订单状态统计数据
     */
    Result<Map<String, Integer>> getOrderStatus(Long userId);
}