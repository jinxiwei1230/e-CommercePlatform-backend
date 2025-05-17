package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.pojo.Coupon;
import com.online.ecommercePlatform.pojo.PageBean;

import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {

    /**
     * 创建优惠券
     * @param coupon 优惠券对象
     * @return 创建后的优惠券
     */
    Coupon createCoupon(Coupon coupon);

    /**
     * 更新优惠券信息
     * @param coupon 优惠券对象
     * @return 更新后的优惠券
     */
    Coupon updateCoupon(Coupon coupon);

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券对象
     */
    Coupon getCouponById(Long id);

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     * @return 是否删除成功
     */
    boolean deleteCoupon(Long id);

    /**
     * 批量删除优惠券
     * @param ids 优惠券ID列表
     * @return 成功删除的优惠券数量
     */
    int deleteCouponBatch(List<Long> ids);

    /**
     * 查询所有优惠券
     * @return 优惠券列表
     */
    List<Coupon> getAllCoupons();

    /**
     * 分页查询优惠券
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageBean<Coupon> getCouponsByPage(int page, int pageSize);

    /**
     * 带条件的分页查询优惠券
     * @param page 页码
     * @param pageSize 每页条数
     * @param coupon 查询条件
     * @return 分页结果
     */
    PageBean<Coupon> getCouponsByPage(int page, int pageSize, Coupon coupon);
    
    /**
     * 分页查询用户的优惠券
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageBean<Coupon> getUserCouponsByPage(Long userId, int page, int pageSize);
} 
