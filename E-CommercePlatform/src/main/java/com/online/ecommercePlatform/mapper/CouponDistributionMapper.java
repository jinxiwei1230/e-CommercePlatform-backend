package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.CouponDistribution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券发放记录数据访问层接口
 */
@Mapper
public interface CouponDistributionMapper {

    /**
     * 插入新的优惠券发放记录
     * @param distribution 优惠券发放记录对象
     * @return 受影响的行数
     */
    int insert(CouponDistribution distribution);
    
    /**
     * 批量插入优惠券发放记录
     * @param distributions 优惠券发放记录列表
     * @return 受影响的行数
     */
    int batchInsert(@Param("list") List<CouponDistribution> distributions);

    /**
     * 更新优惠券发放记录
     * @param distribution 优惠券发放记录对象
     * @return 受影响的行数
     */
    int update(CouponDistribution distribution);

    /**
     * 根据ID查询优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 优惠券发放记录对象
     */
    CouponDistribution findById(Long id);
    
    /**
     * 根据用户ID查询发放记录列表
     * @param userId 用户ID
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> findByUserId(Long userId);
    
    /**
     * 根据优惠券ID查询发放记录列表
     * @param couponId 优惠券ID
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> findByCouponId(Long couponId);

    /**
     * 根据ID删除优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 批量删除优惠券发放记录
     * @param ids 优惠券发放记录ID列表
     * @return 受影响的行数
     */
    int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 根据优惠券ID删除关联的发放记录
     * @param couponId 优惠券ID
     * @return 受影响的行数
     */
    int deleteByCouponId(Long couponId);

    /**
     * 查询所有优惠券发放记录
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> findAll();

    /**
     * 分页查询优惠券发放记录
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> findByPage();

    /**
     * 带条件的分页查询优惠券发放记录
     * @param distribution 查询条件
     * @return 优惠券发放记录列表
     */
    List<CouponDistribution> findByPageWithCondition(CouponDistribution distribution);

    /**
     * 统计优惠券发放记录总数
     * @return 优惠券发放记录总数
     */
    int count();
} 