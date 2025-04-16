package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.online.ecommercePlatform.mapper.CouponDistributionMapper;
import com.online.ecommercePlatform.mapper.CouponMapper;
import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.CouponDistribution;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.service.CouponDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券发放记录服务实现类
 */
@Service
public class CouponDistributionServiceImpl implements CouponDistributionService {

    @Autowired
    private CouponDistributionMapper distributionMapper;
    
    @Autowired
    private CouponMapper couponMapper;
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 创建优惠券发放记录
     * @param distribution 优惠券发放记录对象
     * @return 创建后的优惠券发放记录
     */
    @Override
    @Transactional
    public CouponDistribution createDistribution(CouponDistribution distribution) {
        // 设置创建时间
        if (distribution.getCreateTime() == null) {
            distribution.setCreateTime(LocalDateTime.now());
        }
        
        distributionMapper.insert(distribution);
        return distribution;
    }
    
    /**
     * 批量创建优惠券发放记录
     * @param couponId 优惠券ID
     * @param userIds 用户ID列表
     * @param genderFilter 性别筛选条件
     * @param regionFilter 地区筛选条件
     * @return 创建的记录数量
     */
    @Override
    @Transactional
    public int batchCreateDistributions(Long couponId, List<Long> userIds, String genderFilter, String regionFilter) {
        // 检查优惠券是否存在
        if (couponMapper.findById(couponId) == null) {
            return 0;
        }
        
        List<CouponDistribution> distributions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Long userId : userIds) {
            // 检查用户是否存在
            if (userMapper.findById(userId) == null) {
                continue;
            }
            
            CouponDistribution distribution = new CouponDistribution();
            distribution.setCouponId(couponId);
            distribution.setUserId(userId);
            distribution.setGenderFilter(genderFilter);
            distribution.setRegionFilter(regionFilter);
            distribution.setCreateTime(now);
            
            distributions.add(distribution);
        }
        
        if (distributions.isEmpty()) {
            return 0;
        }
        
        return distributionMapper.batchInsert(distributions);
    }

    /**
     * 更新优惠券发放记录信息
     * @param distribution 优惠券发放记录对象
     * @return 更新后的优惠券发放记录
     */
    @Override
    @Transactional
    public CouponDistribution updateDistribution(CouponDistribution distribution) {
        distributionMapper.update(distribution);
        return distributionMapper.findById(distribution.getDistributionId());
    }

    /**
     * 根据ID查询优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 优惠券发放记录对象
     */
    @Override
    public CouponDistribution getDistributionById(Long id) {
        return distributionMapper.findById(id);
    }
    
    /**
     * 根据用户ID查询优惠券发放记录
     * @param userId 用户ID
     * @return 优惠券发放记录列表
     */
    @Override
    public List<CouponDistribution> getDistributionsByUserId(Long userId) {
        return distributionMapper.findByUserId(userId);
    }
    
    /**
     * 根据优惠券ID查询发放记录
     * @param couponId 优惠券ID
     * @return 优惠券发放记录列表
     */
    @Override
    public List<CouponDistribution> getDistributionsByCouponId(Long couponId) {
        return distributionMapper.findByCouponId(couponId);
    }

    /**
     * 根据ID删除优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteDistribution(Long id) {
        return distributionMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除优惠券发放记录
     * @param ids 优惠券发放记录ID列表
     * @return 成功删除的记录数量
     */
    @Override
    @Transactional
    public int deleteDistributionBatch(List<Long> ids) {
        return distributionMapper.deleteBatch(ids);
    }

    /**
     * 查询所有优惠券发放记录
     * @return 优惠券发放记录列表
     */
    @Override
    public List<CouponDistribution> getAllDistributions() {
        return distributionMapper.findAll();
    }

    /**
     * 分页查询优惠券发放记录
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @Override
    public PageBean<CouponDistribution> getDistributionsByPage(int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        
        // 执行查询
        Page<CouponDistribution> distributionPage = (Page<CouponDistribution>) distributionMapper.findByPage();
        
        // 封装结果
        PageBean<CouponDistribution> pageBean = new PageBean<>();
        pageBean.setItems(distributionPage.getResult());
        pageBean.setTotal(distributionPage.getTotal());
        
        return pageBean;
    }

    /**
     * 带条件的分页查询优惠券发放记录
     * @param page 页码
     * @param pageSize 每页条数
     * @param distribution 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean<CouponDistribution> getDistributionsByPage(int page, int pageSize, CouponDistribution distribution) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        
        // 执行查询
        Page<CouponDistribution> distributionPage = (Page<CouponDistribution>) distributionMapper.findByPageWithCondition(distribution);
        
        // 封装结果
        PageBean<CouponDistribution> pageBean = new PageBean<>();
        pageBean.setItems(distributionPage.getResult());
        pageBean.setTotal(distributionPage.getTotal());
        
        return pageBean;
    }
} 