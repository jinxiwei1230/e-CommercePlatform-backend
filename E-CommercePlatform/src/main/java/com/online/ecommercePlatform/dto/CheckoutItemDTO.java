package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;

// 结算商品项DTO
@Data
public class CheckoutItemDTO {
    private Long cartId;
    private Long productId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Integer stock;           // 添加库存字段
    private BigDecimal freight;      // 添加运费字段
}