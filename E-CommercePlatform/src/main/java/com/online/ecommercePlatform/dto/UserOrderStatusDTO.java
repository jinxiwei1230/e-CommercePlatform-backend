package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 用户订单状态统计DTO
 */
@Data
public class UserOrderStatusDTO {
    private Integer pendingPayment;    // 待付款
    private Integer pendingShipment;   // 待发货
    private Integer pendingReceipt;    // 待收货
    private Integer completed;         // 已完成
    private Integer cancelled;         // 已取消
} 