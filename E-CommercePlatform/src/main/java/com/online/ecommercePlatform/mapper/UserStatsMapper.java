package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.UserTopCategoryConsumptionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户统计Mapper接口
 */
@Mapper
public interface UserStatsMapper {
    
    /**
     * 获取用户季度消费统计
     */
    Map<String, BigDecimal> getUserQuarterlyConsumption(
        @Param("userId") Long userId,
        @Param("year") Integer year,
        @Param("quarter") Integer quarter
    );

    /**
     * 获取用户订单状态统计
     */
    List<Map<String, Object>> getUserOrderStatusCounts(@Param("userId") Long userId);

    /**
     * 获取用户顶级类别消费分布
     */
    List<UserTopCategoryConsumptionDTO> getUserTopCategoryConsumption(
        @Param("userId") Long userId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    /**
     * 获取用户季度消费统计
     * @param year 年份
     * @return 季度消费数据列表
     */
    List<Map<String, Object>> getQuarterlyConsumption(@Param("year") Integer year);

    /**
     * 获取用户订单状态统计
     * @return 订单状态统计数据列表
     */
    List<Map<String, Object>> getOrderStatus();

} 