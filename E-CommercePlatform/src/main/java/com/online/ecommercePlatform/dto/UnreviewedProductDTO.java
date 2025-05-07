package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 未评价商品DTO
 */
@Data
public class UnreviewedProductDTO {
    
    private Long productId;           // 商品ID
    private String productName;       // 商品名称
    private String productImage;      // 商品图片
    private BigDecimal price;         // 商品价格
    private Long orderId;             // 订单ID
    private String orderNumber;       // 订单编号
    private LocalDateTime orderTime;  // 订单完成时间
} 