package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer sales;
    private String categoryId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String image; // 商品主图片链接

}