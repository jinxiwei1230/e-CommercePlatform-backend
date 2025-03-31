package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类信息实体类
 */
@Data
public class Category {
    private Long categoryId; // 分类唯一标识（主键）
    private String name; // 分类名称
    private Long parentId; // 父分类 ID（支持多级分类，顶级分类为 0）
    private LocalDateTime createTime; // 创建时间
    private List<Category> children; // 子分类列表,当没有子分类时，该字段为null或空列表
}