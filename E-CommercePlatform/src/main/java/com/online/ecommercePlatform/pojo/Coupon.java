package com.online.ecommercePlatform.pojo;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 优惠券信息实体类
 */
@Data
public class Coupon {
    private Long couponId; // 优惠券唯一标识（主键）
    private String type; // 类型（满减 / 折扣 / 固定金额）
    private Double discountValue; // 折扣值（满减金额 / 折扣比例）
    private Double minOrderAmount; // 最低使用金额
    private LocalDateTime startTime; // 生效时间
    private LocalDateTime endTime; // 失效时间
    private String genderFilter; // 性别筛选条件
    private String regionFilter; // 地区筛选条件
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 信息更新时间
}
