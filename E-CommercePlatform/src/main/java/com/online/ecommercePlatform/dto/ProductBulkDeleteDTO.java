package com.online.ecommercePlatform.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for bulk deleting products.
 */
@Data
public class ProductBulkDeleteDTO {
    @NotEmpty(message = "商品ID列表不能为空") // 确保列表本身不为null且不为空
    private List<Long> ids;
} 