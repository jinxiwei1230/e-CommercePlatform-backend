package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 添加新商品请求的数据传输对象
 */
@Data
public class ProductCreateDTO {
    private String name; // 商品名称
    private Long categoryId; // 商品分类 ID
    private BigDecimal price; // 价格 (使用 BigDecimal 保证精度)
    private Integer stock; // 库存
    private BigDecimal shippingFee; // 运费 (Product 实体中是 freight)
    private String description; // 商品描述
    // 根据需要可以添加其他字段，如品牌、规格等，但当前接口定义不包含
} 