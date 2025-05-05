package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;

// 退款申请请求DTO
@Data
public class RefundRequestDTO {
    private String reason;
    private BigDecimal refundAmount;
    private String type;
}