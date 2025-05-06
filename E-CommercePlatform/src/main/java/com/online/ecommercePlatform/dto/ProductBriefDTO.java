package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.math.BigDecimal; // 使用 BigDecimal 更精确地表示价格

/**
 * 商品列表项（简要信息）DTO
 */
@Data
public class ProductBriefDTO {
    private Long productId;
    private String name; // 商品名称
    private String mainImageUrl; // 主图 URL
    private Double price; // 价格 (如果数据库中是 DECIMAL，考虑使用 BigDecimal)
    private Integer stock; // 库存
    private Integer sales; // 销量
    private Double shippingFee; // 运费 (根据您的需求，Product 实体中有 freight 字段)
    private String createdAt; // 创建时间 (格式化字符串，如: "2025-04-18 17:58")
    private String categoryName; // 分类名称
} 