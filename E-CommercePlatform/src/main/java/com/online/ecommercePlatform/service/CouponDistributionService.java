package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.pojo.CouponDistribution;
import com.online.ecommercePlatform.pojo.PageBean;

import java.util.List;

/**
 * 优惠券发放记录服务接口
 */
public interface CouponDistributionService {

    /**
     * 创建优惠券发放记录
     * @param distribution 优惠券发放记录对象
     * @return 创建后的优惠券发放记录
     */
    CouponDistribution createDistribution(CouponDistribution distribution);
    
    /**
     * 批量创建优惠券发放记录
     * @param couponId 优惠券ID
     * @param userIds 用户ID列表
     * @param genderFilter 性别筛选条件
     * @param regionFilter 地区筛选条件
     * @return 创建的记录数量
     */
    int batchCreateDistributions(Long couponId, List<Long> userIds, String genderFilter, String regionFilter);

    /**
     * 更新优惠券发放记录信息
     * @param distribution 优惠券发放记录对象
     * @return 更新后的优惠券发放记录
     */
    CouponDistribution updateDistribution(CouponDistribution distribution);

    /**
     * 根据ID查询优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 优惠券发放记录对象
     */
    CouponDistribution getDistributionById(Long id);
    
    /**
     * 根据用户ID查询优惠券发放记录
     * @param userId 用户ID
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> getDistributionsByUserId(Long userId);
    
    /**
     * 根据优惠券ID查询发放记录
     * @param couponId 优惠券ID
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> getDistributionsByCouponId(Long couponId);

    /**
     * 根据ID删除优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 是否删除成功
     */
    boolean deleteDistribution(Long id);

    /**
     * 批量删除优惠券发放记录
     * @param ids 优惠券发放记录ID列表
     * @return 成功删除的记录数量
     */
    int deleteDistributionBatch(List<Long> ids);

    /**
     * 查询所有优惠券发放记录
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> getAllDistributions();

    /**
     * 分页查询优惠券发放记录
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageBean<CouponDistribution> getDistributionsByPage(int page, int pageSize);

    /**
     * 带条件的分页查询优惠券发放记录
     * @param page 页码
     * @param pageSize 每页条数
     * @param distribution 查询条件
     * @return 分页结果
     */
    PageBean<CouponDistribution> getDistributionsByPage(int page, int pageSize, CouponDistribution distribution);
} 