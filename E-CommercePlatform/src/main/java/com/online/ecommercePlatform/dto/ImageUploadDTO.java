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

    // 可以添加其他字段，如 sortOrder, isMain 等，如果前端会传递的话
    // private Integer sortOrder;
    // private Boolean isMain;
} 