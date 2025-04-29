package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 加入购物车请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAddDTO {
    
    @NotBlank(message = "商品ID不能为空")
    private String product_id; // 商品ID
    
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity; // 商品数量
} 