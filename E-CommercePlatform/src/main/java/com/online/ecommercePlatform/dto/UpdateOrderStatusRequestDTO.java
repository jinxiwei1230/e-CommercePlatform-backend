package com.online.ecommercePlatform.dto;

import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO {
    private String status; // 新的订单状态
}