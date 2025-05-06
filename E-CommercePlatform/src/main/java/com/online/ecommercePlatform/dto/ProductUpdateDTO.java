package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 更新商品信息请求的数据传输对象
 * 所有字段均为可选，仅更新提供的字段。
 */
@Data
public class ProductUpdateDTO {
    private String name; // 商品名称
    private Long categoryId; // 商品分类 ID
    private BigDecimal price; // 价格
    private Integer stock; // 库存
    private BigDecimal shippingFee; // 运费
    private String description; // 商品描述
    // 根据需要可以添加其他允许更新的字段
} 