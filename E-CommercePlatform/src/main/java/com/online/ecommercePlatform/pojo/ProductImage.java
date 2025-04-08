package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品图片实体类
 */
@Data
public class ProductImage {

    private Long id; //图片ID，主键，自增长
    private Long productId;  //关联的商品ID
    private String imageUrl; // 图片URL地址
    private Integer sortOrder = 0;  //图片URL地址
    private Boolean isMain = false; // 是否为主图，默认false
    private LocalDateTime createdTime; // 创建时间
}