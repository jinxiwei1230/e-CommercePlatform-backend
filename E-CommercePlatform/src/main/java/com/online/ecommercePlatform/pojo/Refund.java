package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Refund {
    private Long refundId;
    private Long orderId;
    private Long userId;
    private String reason;
    private BigDecimal refundAmount;
    private String type;
    private String status;
    private Timestamp createTime;
    private Timestamp updateTime;
}