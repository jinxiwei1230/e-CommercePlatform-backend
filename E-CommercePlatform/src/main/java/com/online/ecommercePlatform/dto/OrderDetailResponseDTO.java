package com.online.ecommercePlatform.dto;

import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.Coupon;
import lombok.Data;

import java.util.List;

// 订单详情响应DTO
@Data
public class OrderDetailResponseDTO {
    private OrderDetailDTO order;
    private List<OrderItemDTO> items;
    private Address address;
    private Coupon coupon;
}