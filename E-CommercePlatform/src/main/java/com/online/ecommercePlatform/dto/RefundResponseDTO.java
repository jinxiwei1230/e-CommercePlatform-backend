package com.online.ecommercePlatform.dto;

import lombok.Data;

// 退款申请响应DTO
@Data
public class RefundResponseDTO {
    private Long refundId;
    private String status;
}