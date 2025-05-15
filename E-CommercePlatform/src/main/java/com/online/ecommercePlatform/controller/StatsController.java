package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.service.OrderService;
import com.online.ecommercePlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2025/5/14 11:24
 **/
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户最常购买类别接口
     */
    @GetMapping("/user/favorite-category")
    public Result<?> category() {
        return orderService.category();
    }
    /**
     * 用户订单总览接口
     */
    @GetMapping("/user/order-summary")
    public Result<?> summary() {
        return orderService.summary();
    }

    /**
     * 用户订单状态统计接口
     */
    @GetMapping("/user/order-status")
    public Result<?> orderStatus() {
        return orderService.orderStatus();
    }
    /**
     * 销售数据概览接口
     */
    @GetMapping("admin/sales-overview")
    public Result<?> salesOverview() {
        return orderService.salesOverview();
    }

    /**
     * 用户数据概览接口
     */
    @GetMapping("admin/users-overview")
    public Result<?> usersOverview() {
        return orderService.usersOverview();
    }
}

