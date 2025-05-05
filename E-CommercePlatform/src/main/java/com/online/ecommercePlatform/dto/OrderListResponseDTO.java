package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.util.List;

// 订单列表响应DTO
@Data
public class OrderListResponseDTO {
    private Long total;
    private List<OrderSummaryDTO> items;
}