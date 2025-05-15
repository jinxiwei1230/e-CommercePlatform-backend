package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.UserQuarterlyConsumptionDTO;
import com.online.ecommercePlatform.dto.UserOrderStatusDTO;
import com.online.ecommercePlatform.dto.UserTopCategoryConsumptionDTO;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.UserStatsService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 用户统计控制器
 */
@RestController
@RequestMapping("/api/user/stats")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 从请求头中解析并验证token，获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null || jwtUtil.isTokenExpired(token)) {
                return null;
            }

            String userIdStr = jwt.getClaim("userId").asString();
            return StringUtils.hasText(userIdStr) ? Long.valueOf(userIdStr) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户季度消费统计
     */
    @GetMapping("/consumption/quarterly")
    public Result<Map<String, Map<String, BigDecimal>>> getQuarterlyConsumption(
            @RequestParam(required = false) Integer year,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        return userStatsService.getQuarterlyConsumption(userId, year);
    }

    /**
     * 获取用户订单状态统计
     */
    @GetMapping("/orders/status")
    public Result<Map<String, Integer>> getOrderStatus(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        return userStatsService.getOrderStatus(userId);
    }

} 