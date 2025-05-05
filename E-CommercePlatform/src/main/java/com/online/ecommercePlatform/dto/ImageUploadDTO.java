package com.online.ecommercePlatform.dto;

import lombok.Data;

/**
 * 用于接收图片上传数据的 DTO
 */
@Data
public class ImageUploadDTO {

    private Long productId; // 关联的商品ID

    // 包含完整 Data URL (data:image/...;base64,...) 的 Base64 字符串
    private String imageDataWithPrefix; 

    // 图片排序值 (可选)
    private Integer sortOrder;
    // 是否为主图 (可选)
    private Boolean isMain;
} 