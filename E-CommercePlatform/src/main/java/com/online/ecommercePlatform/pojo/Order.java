package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单信息实体类
 */
@Data
public class Order {
    private Long orderId; // 订单唯一标识（主键）
    private Long userId; // 用户 ID（外键关联用户表）
    private Double totalAmount; // 订单总金额（含运费）
    private String status; // 订单状态（待支付 / 已支付 / 已发货 / 已完成 / 已退款）
    private String paymentMethod; // 支付方式
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 状态更新时间
    private Long couponId; //订单使用的优惠券id
    private Double discountAmount; //订单折扣
}