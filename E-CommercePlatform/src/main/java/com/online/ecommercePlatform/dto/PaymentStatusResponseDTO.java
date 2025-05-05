package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

// 支付状态响应DTO
@Data
public class PaymentStatusResponseDTO {
    private String status;
    private Timestamp paymentTime;
    private String paymentMethod;
    private BigDecimal paymentAmount;
}