package com.online.ecommercePlatform.dto;

import lombok.Data;

// 支付确认请求DTO
@Data
public class PaymentConfirmRequestDTO {
    private String transactionId; // 支付网关返回的交易ID
}