package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 优惠券数据访问层接口
 */
@Mapper
public interface CouponMapper {

    List<Long> selectEligibleUserIds(@Param("genderFilter") String genderFilter, @Param("regionFilter") String regionFilter);

    /**
     * 插入新的优惠券
     * @param coupon 优惠券对象
     * @return 受影响的行数
     */
    int insert(Coupon coupon);

    /**
     * 更新优惠券信息
     * @param coupon 优惠券对象
     * @return 受影响的行数
     */
    int update(Coupon coupon);

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券对象
     */
    Coupon findById(Long id);

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 批量删除优惠券
     * @param ids 优惠券ID列表
     * @return 受影响的行数
     */
    int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 查询所有优惠券
     * @return 优惠券列表
     */
    List<Coupon> findAll();

    /**
     * 分页查询优惠券
     * @return 优惠券列表
     */
    List<Coupon> findByPage();

    /**
     * 带条件的分页查询优惠券
     * @param coupon 查询条件
     * @return 优惠券列表
     */
    List<Coupon> findByPageWithCondition(Coupon coupon);

    /**
     * 统计优惠券总数
     * @return 优惠券总数
     */
    int count();

    /**
     * 根据用户ID查询优惠券
     * @param userId 用户ID
     * @return 优惠券列表
     */
    List<Coupon> findByUserId(@Param("userId") Long userId);
} 
