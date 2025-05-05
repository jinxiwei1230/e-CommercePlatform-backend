package com.online.ecommercePlatform.dto;

import lombok.Data;

// 商品预览DTO
@Data
public class ProductPreviewDTO {
    private Long productId;
    private String name;
    private Integer quantity;
}