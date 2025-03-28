package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单明细信息实体类
 */
@Data
public class OrderDetail {
    private Long orderDetailId; // 订单明细唯一标识（主键）
    private Long orderId; // 订单 ID（外键关联订单表）
    private Long productId; // 商品 ID（外键关联商品表）
    private Integer quantity; // 购买数量
    private Double unitPrice; // 商品单价（下单时快照）
    private LocalDateTime createTime; // 创建时间
}
