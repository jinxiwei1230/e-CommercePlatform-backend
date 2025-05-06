package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartProductListDTO {
    private Long cartId;           // 购物车ID
    private Long userId;          // 用户ID
    private Long productId;       // 商品ID
    private Integer quantity;     // 商品数量
    private LocalDateTime createTime; // 购物车记录创建时间
    private String name;          // 商品名称
    private String description;   // 商品简介
    private BigDecimal price;     // 商品单价
    private Integer stock;        // 商品库存数量
    private Integer sales;        // 商品销量
    private BigDecimal freight;   // 商品运费
    private Long categoryId;      // 商品所属分类ID
}