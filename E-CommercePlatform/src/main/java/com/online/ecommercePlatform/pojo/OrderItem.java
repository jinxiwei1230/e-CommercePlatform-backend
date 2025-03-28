package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long orderItemId;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private LocalDateTime createTime;
}