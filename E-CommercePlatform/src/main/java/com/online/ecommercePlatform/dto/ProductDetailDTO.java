package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品详情DTO，用于返回商品详细信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private String productId;   // 商品ID
    private String name;        // 商品名称
    private String description; // 商品描述
    private Double price;       // 当前价格
    private Integer stock;      // 库存
    private Integer sales;      // 销量
    private String categoryId;  // 分类ID
    private String categoryName; // 分类名称
    private List<ProductImageDTO> images; // 商品图片

    /**
     * 商品图片DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDTO {
        private String imageId;   // 图片ID
        // 注意：此字段包含完整的 Base64 Data URL
        private String imageUrl;  // 图片URL (Base64 Data URL)
        private Integer sortOrder; // 排序
    }
} 