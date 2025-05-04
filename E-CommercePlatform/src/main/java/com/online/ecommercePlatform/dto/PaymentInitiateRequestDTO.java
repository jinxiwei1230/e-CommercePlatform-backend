package com.online.ecommercePlatform.dto;

import lombok.Data;

// 支付发起请求DTO
@Data
public class PaymentInitiateRequestDTO {
    private String paymentMethod; // 支付方式
}