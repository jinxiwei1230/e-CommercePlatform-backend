package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;

// 订单项DTO
@Data
public class OrderItemDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}