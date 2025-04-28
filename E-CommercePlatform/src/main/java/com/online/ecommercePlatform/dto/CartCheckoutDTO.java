package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车结算DTO
 */
@Data
public class CartCheckoutDTO {
    private List<CartItemDTO> items;      // 结算商品列表
    private BigDecimal totalAmount;      // 商品总金额
    private BigDecimal totalFreight;     // 总运费
    private BigDecimal paymentAmount;    // 实际支付金额(商品总金额+运费)

    /**
     * 结算商品项DTO
     */
    @Data
    public static class CartItemDTO {
        private Long productId;          // 商品ID
        private String productName;      // 商品名称
        private Integer quantity;        // 购买数量
        private BigDecimal price;       // 商品单价
        private BigDecimal subtotal;     // 商品小计(单价*数量)
        private BigDecimal freight;     // 运费
    }
}