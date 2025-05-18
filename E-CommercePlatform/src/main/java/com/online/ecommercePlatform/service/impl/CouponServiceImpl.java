package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.online.ecommercePlatform.mapper.CouponDistributionMapper;
import com.online.ecommercePlatform.mapper.CouponMapper;
import com.online.ecommercePlatform.pojo.Coupon;
import com.online.ecommercePlatform.pojo.CouponDistribution;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务实现类
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;
    
    @Autowired
    private CouponDistributionMapper distributionMapper;

    /**
     * 创建优惠券
     * @param coupon 优惠券对象
     * @return 创建后的优惠券
     */
    @Override
    @Transactional
    public Coupon createCouponAndDistribute(Coupon coupon) {
        // 1. 保存优惠券
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insert(coupon);

        // 2. 根据性别和地区筛选用户
        String genderFilter = coupon.getGenderFilter();
        String regionFilter = coupon.getRegionFilter();
        List<Long> eligibleUserIds = couponMapper.selectEligibleUserIds(genderFilter, regionFilter);
        System.out.println("-------------------------------------------");
        System.out.println("genderFilter: " + genderFilter);
        System.out.println("regionFilter: " + regionFilter);
        System.out.println("eligibleUserIds: " + eligibleUserIds);
        System.out.println("-------------------------------------------");
        
        // 3. 为每个符合条件的用户创建分发记录
        for (Long userId : eligibleUserIds) {
            CouponDistribution distribution = new CouponDistribution();
            distribution.setCouponId(coupon.getCouponId());
            distribution.setUserId(userId);
            distribution.setStatus("未使用");
            distribution.setGenderFilter(genderFilter);
            distribution.setRegionFilter(regionFilter);
            distribution.setCreateTime(LocalDateTime.now());
            distributionMapper.insert(distribution);
        }

        return coupon;
    }
//    @Override
//    @Transactional
//    public Coupon createCoupon(Coupon coupon) {
//        // 设置初始状态为未使用
//        if (coupon.getStatus() == null) {
//            coupon.setStatus("未使用");
//        }
//
//        // 设置创建时间
//        if (coupon.getCreateTime() == null) {
//            coupon.setCreateTime(LocalDateTime.now());
//        }
//
//        couponMapper.insert(coupon);
//        return coupon;
//    }

    /**
     * 更新优惠券信息
     * @param coupon 优惠券对象
     * @return 更新后的优惠券
     */
    @Override
    @Transactional
    public Coupon updateCoupon(Coupon coupon) {
        couponMapper.update(coupon);
        return couponMapper.findById(coupon.getCouponId());
    }

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券对象
     */
    @Override
    public Coupon getCouponById(Long id) {
        return couponMapper.findById(id);
    }

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteCoupon(Long id) {
        // 先删除关联的发放记录
        distributionMapper.deleteByCouponId(id);
        // 再删除优惠券
        return couponMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除优惠券
     * @param ids 优惠券ID列表
     * @return 成功删除的优惠券数量
     */
    @Override
    @Transactional
    public int deleteCouponBatch(List<Long> ids) {
        // 先删除关联的发放记录
        for (Long id : ids) {
            distributionMapper.deleteByCouponId(id);
        }
        // 再批量删除优惠券
        return couponMapper.deleteBatch(ids);
    }

    /**
     * 查询所有优惠券
     * @return 优惠券列表
     */
    @Override
    public List<Coupon> getAllCoupons() {
        return couponMapper.findAll();
    }

    /**
     * 分页查询优惠券
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @Override
    public PageBean<Coupon> getCouponsByPage(int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        
        // 执行查询
        Page<Coupon> couponPage = (Page<Coupon>) couponMapper.findByPage();
        
        // 封装结果
        PageBean<Coupon> pageBean = new PageBean<>();
        pageBean.setItems(couponPage.getResult());
        pageBean.setTotal(couponPage.getTotal());
        
        return pageBean;
    }

    /**
     * 带条件的分页查询优惠券
     * @param page 页码
     * @param pageSize 每页条数
     * @param coupon 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean<Coupon> getCouponsByPage(int page, int pageSize, Coupon coupon) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        
        // 执行查询
        Page<Coupon> couponPage = (Page<Coupon>) couponMapper.findByPageWithCondition(coupon);
        
        // 封装结果
        PageBean<Coupon> pageBean = new PageBean<>();
        pageBean.setItems(couponPage.getResult());
        pageBean.setTotal(couponPage.getTotal());
        
        return pageBean;
    }
    
    /**
     * 分页查询用户的优惠券
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @Override
    public PageBean<Coupon> getUserCouponsByPage(Long userId, int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        
        // 执行查询
        // 使用CouponMapper中的findByUserId方法查询用户的优惠券
        Page<Coupon> couponPage = (Page<Coupon>) couponMapper.findByUserId(userId);
        
        // 封装结果
        PageBean<Coupon> pageBean = new PageBean<>();
        pageBean.setItems(couponPage.getResult());
        pageBean.setTotal(couponPage.getTotal());
        
        return pageBean;
    }
} 
