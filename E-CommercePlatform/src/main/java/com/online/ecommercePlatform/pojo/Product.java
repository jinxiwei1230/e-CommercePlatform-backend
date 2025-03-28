package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品基本信息实体类
 */
@Data
public class Product {
    private Long productId; // 商品唯一标识（主键）
    private String name; // 商品名称
    private String description; // 商品简介
    private Double price; // 单价
    private Integer stock; // 库存数量
    private Double freight; // 运费
    private Long categoryId; // 所属分类 ID（外键关联商品分类表）
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 信息更新时间
}