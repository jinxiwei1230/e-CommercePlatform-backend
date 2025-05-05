package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

// 订单详情DTO
@Data
public class OrderDetailDTO {
    private Long orderId;
    private String orderNo;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private BigDecimal discountAmount;
    private BigDecimal freight;
    private String remark;
    private Timestamp createTime;
    private Timestamp updateTime;
}
