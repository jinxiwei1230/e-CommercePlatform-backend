package com.online.ecommercePlatform.dto;

import lombok.Data;

// 订单取消响应DTO
@Data
public class OrderCancelResponseDTO {
    private Long orderId;
    private String status;
}