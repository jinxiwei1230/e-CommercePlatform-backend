package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 搜索商品的请求参数封装到 DTO 中
 */
@Data
public class ProductQueryDTO {
    private String name; // 商品名称模糊搜索
    private Long categoryId; // 商品分类 ID
    private Integer page = 1; // 当前页码
    private Integer pageSize = 10; // 每页数量 (原为20，改为10与接口定义一致)
    private String sortBy; // 排序字段 (如: price, sales, createdAt)
    private String sortOrder = "desc"; // 排序顺序 (asc 或 desc)
    private BigDecimal minPrice; // 最低价格
    private BigDecimal maxPrice; // 最高价格

    // 可选：提供一个获取默认排序字段的方法
    public String getSortByOrDefault() {
        if (this.sortBy == null || this.sortBy.trim().isEmpty()) {
            return "createTime"; // 假设 Product 实体中的创建时间字段是 createTime
        }
        // 未来可以增加对 sortBy 字段的白名单校验
        return this.sortBy;
    }
} 