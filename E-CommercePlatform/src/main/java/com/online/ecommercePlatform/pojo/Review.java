package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品评价信息实体类
 */
@Data
public class Review {
    private Long reviewId; // 评价唯一标识（主键）
    private Long productId; // 商品 ID（外键关联商品表）
    private Long userId; // 用户 ID（外键关联用户表）
    private Long orderId; // 订单 ID（外键关联订单表，确保已购买）
    private Integer rating; // 评分（1-5 星）
    private String content; // 评价内容
    private LocalDateTime createTime; // 创建时间
}