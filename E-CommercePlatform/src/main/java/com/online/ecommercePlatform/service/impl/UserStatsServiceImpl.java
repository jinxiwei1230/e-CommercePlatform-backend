package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.UserQuarterlyConsumptionDTO;
import com.online.ecommercePlatform.dto.UserOrderStatusDTO;
import com.online.ecommercePlatform.dto.UserTopCategoryConsumptionDTO;
import com.online.ecommercePlatform.mapper.UserStatsMapper;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 用户统计服务实现类
 */
@Service
public class UserStatsServiceImpl implements UserStatsService {

    @Autowired
    private UserStatsMapper userStatsMapper;

    @Override
    public Result<Map<String, Map<String, BigDecimal>>> getQuarterlyConsumption(Long userId, Integer year) {
        if (year == null) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();
        for (int quarter = 1; quarter <= 4; quarter++) {
            Map<String, BigDecimal> quarterData = userStatsMapper.getUserQuarterlyConsumption(userId, year, quarter);
            result.put("Q" + quarter, quarterData);
        }

        return Result.success(result);
    }

    @Override
    public Result<Map<String, Integer>> getOrderStatus(Long userId) {
        List<Map<String, Object>> statusList = userStatsMapper.getUserOrderStatusCounts(userId);
        Map<String, Integer> statusCounts = new HashMap<>();
        for (Map<String, Object> status : statusList) {
            statusCounts.put(
                (String) status.get("status"),
                ((Number) status.get("count")).intValue()
            );
        }
        return Result.success(statusCounts);
    }


} 