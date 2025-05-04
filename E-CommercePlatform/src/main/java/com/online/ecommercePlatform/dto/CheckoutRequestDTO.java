package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.util.List;

// 购物车结算请求DTO
@Data
public class CheckoutRequestDTO {
    private List<Long> cartIds; // 购物车商品ID列表
    private Long addressId;     // 预选地址ID（可选）
}