package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 购物车基本信息
 */
@Data
public class Cart {
    private Long cartId; // 购物车ID，唯一标识购物车
    private Long userId; // 用户ID
    private Long productId; // 商品ID
    private Integer quantity; // 商品数量
    private LocalDateTime createTime; // 购物车创建时间
}
