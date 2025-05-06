package com.online.ecommercePlatform.dto;

import lombok.Data;
// import javax.validation.constraints.NotNull; // 旧的 javax 命名空间
import jakarta.validation.constraints.NotNull; // 新的 jakarta 命名空间

/**
 * DTO for updating the main status of a product image.
 */
@Data
public class ImageMainStatusUpdateDTO {
    // JSR 303 Bean Validation: @NotNull ensures the field is present and not null.
    // Spring will automatically validate this if @Valid is used in the controller.
    @NotNull(message = "isMain 字段为必需且必须是布尔值")
    private Boolean isMain;
} 