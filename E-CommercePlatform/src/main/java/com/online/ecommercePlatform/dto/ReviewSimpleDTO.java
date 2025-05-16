package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 简化评价DTO，仅用于评价权限验证
 */
@Data
public class ReviewSimpleDTO {
    
    private Long reviewId;     // 评价ID
    
    public ReviewSimpleDTO(Long reviewId) {
        this.reviewId = reviewId;
    }
    
    public ReviewSimpleDTO() {
    }
} 