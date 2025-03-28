package com.online.ecommercePlatform.pojo;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 地址信息实体类
 */
@Data
public class Address {
    private Long addressId; // 地址唯一标识（主键）
    private Long userId; // 用户 ID（外键关联用户表）
    private String recipientName; // 收货人姓名
    private String phone; // 收货人电话
    private String addressDetail; // 详细地址（街道 / 门牌号）
    private String city; // 城市
    private String state; // 省份 / 州
    private String postalCode; // 邮政编码
    private Boolean isDefault; // 是否为默认地址
    private LocalDateTime createTime; // 创建时间
}
