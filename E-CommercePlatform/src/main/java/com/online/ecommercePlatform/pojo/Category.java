package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类信息实体类
 */
@Data
public class Category {
    private Long categoryId; // 分类唯一标识（主键）
    private String name; // 分类名称
    private Long parentId; // 父分类 ID（支持多级分类，顶级分类为 0）
    private LocalDateTime createTime; // 创建时间
}