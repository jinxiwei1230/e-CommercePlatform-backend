package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

// 订单摘要DTO
@Data
public class OrderSummaryDTO {
    private Long orderId;
    private String orderNo;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp createTime;
    private List<ProductPreviewDTO> productPreview;
}