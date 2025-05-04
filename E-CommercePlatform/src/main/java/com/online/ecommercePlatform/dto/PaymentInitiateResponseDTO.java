package com.online.ecommercePlatform.dto;

import lombok.Data;

// 支付发起响应DTO
@Data
public class PaymentInitiateResponseDTO {
    private Long orderId;
    private String paymentUrl;
    private String qrCode; // 可选，支付二维码
}