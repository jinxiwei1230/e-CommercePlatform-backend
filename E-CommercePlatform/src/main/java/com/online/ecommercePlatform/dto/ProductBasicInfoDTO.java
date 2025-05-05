package com.online.ecommercePlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasicInfoDTO {
    @Schema(description = "商品ID", example = "123456")
    private Long productId;

    @Schema(description = "商品名称", example = "高端智能手机")
    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @Schema(description = "商品主图URL", example = "https://example.com/image1.jpg")
    private String mainImageUrl;

    @Schema(description = "商品售价（元）", example = "5999.00")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal sellingPrice;

    @Schema(description = "商品简短描述", example = "旗舰级智能手机，搭载最新处理器")
    @Size(max = 100, message = "描述不能超过100个字符")
    private String briefDescription;

    @Schema(description = "商品销量", example = "20000")
    @Min(value = 0, message = "销量不能为负数")
    private Integer sales;

    @Schema(description = "商品库存", example = "20000")
    @Min(value = 0, message = "销量不能为负数")
    private Integer stock;
}