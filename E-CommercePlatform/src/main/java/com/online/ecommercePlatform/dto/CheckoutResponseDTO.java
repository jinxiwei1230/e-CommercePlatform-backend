package com.online.ecommercePlatform.dto;

import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.Coupon;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// 购物车结算响应DTO
@Data
public class CheckoutResponseDTO {
    private List<CheckoutItemDTO> checkoutItems; // 结算商品清单
    private BigDecimal totalAmount;              // 总金额
    private BigDecimal freight;                  // 运费
    private List<Address> addresses;          // 可用地址列表
    private List<Coupon> availableCoupons;    // 可用优惠券列表
}