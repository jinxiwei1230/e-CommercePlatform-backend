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
    // 注意：此字段存储完整的 Base64 Data URL (例如 data:image/png;base64,...)
    // 数据库对应字段类型应为 TEXT 或 LONGTEXT
    private String imageUrl; // 图片URL地址 (存储Base64 Data URL)
    private Integer sortOrder = 0;  //图片URL地址
    private Boolean isMain = false; // 是否为主图，默认false
    private LocalDateTime createdTime; // 创建时间
}