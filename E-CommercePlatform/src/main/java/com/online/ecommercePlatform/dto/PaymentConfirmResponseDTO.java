package com.online.ecommercePlatform.dto;

import lombok.Data;

// 支付确认响应DTO
@Data
public class PaymentConfirmResponseDTO {
    private Long orderId;
    private String status;
}