package com.online.ecommercePlatform.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 评价请求DTO
 */
@Data
public class ReviewDTO {
    
    private Long productId;    // 商品ID
    
    private Long orderId;      // 订单ID
    
    @Range(min = 1, max = 5, message = "评分必须在1-5之间")
    private Integer rating;    // 评分（1-5星）
    
    private String content;    // 评价内容
} 