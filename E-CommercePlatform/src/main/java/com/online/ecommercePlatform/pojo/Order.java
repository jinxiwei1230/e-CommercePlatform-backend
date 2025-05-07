package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单信息实体类
 */
@Data
public class Order {
    private Long orderId; // 订单唯一标识（主键）
    private Long userId; // 用户 ID（外键关联用户表）
    private BigDecimal totalAmount; // 订单总金额（含运费）
    private String status; // 订单状态（待支付 / 已支付 / 已发货 / 已完成 / 已退款）
    private String paymentMethod; // 支付方式
    private String paymentUrl; //支付url
    private String qrCode; //支付二维码
    private Long couponId; //订单使用的优惠券id
    private Long addressId;
    private BigDecimal discountAmount; //订单折扣
    private String remark; // 备注
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 状态更新时间
    private String reviewStatus; // 评价状态（未评价 / 已评价）
}
