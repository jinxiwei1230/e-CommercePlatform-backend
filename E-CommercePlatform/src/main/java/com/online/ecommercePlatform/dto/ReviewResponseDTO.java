package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评价响应DTO
 */
@Data
public class ReviewResponseDTO {
    
    private Long reviewId;        // 评价ID
    private Long productId;       // 商品ID
    private String productName;   // 商品名称
    private Long userId;          // 用户ID
    private String username;      // 用户名
    private Long orderId;         // 订单ID
    private Integer rating;       // 评分
    private String content;       // 评价内容
    private LocalDateTime createTime;  // 创建时间
} 