package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;

// 订单创建响应DTO
@Data
public class OrderCreateResponseDTO {
    private Long orderId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal freight;  // 订单运费
    private String paymentUrl;
    private String status;
}