package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.util.List;

// 订单创建请求DTO
@Data
public class OrderCreateRequestDTO {
    private List<Long> cartIds;   // 购物车商品ID列表
    private Long addressId;       // 地址ID
    private Long couponId;        // 优惠券ID（可选）
    private String paymentMethod; // 支付方式
}