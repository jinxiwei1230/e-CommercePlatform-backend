package com.online.ecommercePlatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryHotProductsDTO {
    private Long categoryId;
    private String categoryName;
    private List<ProductBasicInfoDTO> hotProducts;
}