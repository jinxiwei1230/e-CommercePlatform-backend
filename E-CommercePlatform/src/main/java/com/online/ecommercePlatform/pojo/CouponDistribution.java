package com.online.ecommercePlatform.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券发放记录实体类
 */
@Data
public class CouponDistribution {
    private Long distributionId; // 发放记录唯一标识（主键）
    private Long couponId; // 优惠券 ID（外键关联优惠券表）
    private Long userId; // 用户 ID（外键关联用户表）
    private String genderFilter; // 性别筛选条件
    private String regionFilter; // 地区筛选条件
    private LocalDateTime createTime; // 发放时间
}