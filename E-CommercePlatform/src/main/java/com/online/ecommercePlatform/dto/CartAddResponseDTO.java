package com.online.ecommercePlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加入购物车响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAddResponseDTO {
    private String cart_id; // 购物车项ID
} 