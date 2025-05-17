package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.ReviewResponseDTO;
import com.online.ecommercePlatform.dto.ReviewSimpleDTO;
import com.online.ecommercePlatform.dto.UnreviewedProductDTO;
import com.online.ecommercePlatform.pojo.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评价Mapper接口
 */
@Mapper
public interface ReviewMapper {
    
    /**
     * 查询用户已完成且尚未有评价记录的订单商品列表
     * @param userId 用户ID
     * @return 可评价商品列表
     */
    List<UnreviewedProductDTO> selectUnreviewedProducts(@Param("userId") Long userId);
    
    /**
     * 新增评价
     * @param review 评价信息
     * @return 影响的行数
     */
    int insert(Review review);
    
    /**
     * 查询指定商品的评价列表
     * @param productId 商品ID
     * @return 评价列表
     */
    List<ReviewResponseDTO> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 查询用户的评价列表
     * @param userId 用户ID
     * @return 评价列表
     */
    List<ReviewResponseDTO> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户的评价ID列表（简化版）
     * @param userId 用户ID
     * @return 评价ID列表
     */
    List<ReviewSimpleDTO> selectUserReviewIds(@Param("userId") Long userId);
    
    /**
     * 根据ID查询评价详情
     * @param reviewId 评价ID
     * @return 评价详情
     */
    ReviewResponseDTO selectById(@Param("reviewId") Long reviewId);
    
    /**
     * 更新评价
     * @param review 评价信息
     * @return 影响的行数
     */
    int update(Review review);
    
    /**
     * 删除评价
     * @param reviewId 评价ID
     * @param userId 用户ID（确保用户只能删除自己的评价）
     * @return 影响的行数
     */
    int delete(@Param("reviewId") Long reviewId, @Param("userId") Long userId);
    
    /**
     * 检查评价是否存在且属于指定用户
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @return 评价信息
     */
    Review checkReviewBelongsToUser(@Param("reviewId") Long reviewId, @Param("userId") Long userId);
    
    /**
     * 检查用户是否已对该订单商品评价
     * @param userId 用户ID
     * @param productId 商品ID
     * @param orderId 订单ID
     * @return 评价数量
     */
    int checkReviewExists(@Param("userId") Long userId, @Param("productId") Long productId, @Param("orderId") Long orderId);
    
    /**
     * 检查用户是否已对该商品评价（不关联订单）
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 评价数量
     */
    int checkReviewExistsWithoutOrder(@Param("userId") Long userId, @Param("productId") Long productId);
    
    /**
     * 统计指定商品的评价总数
     * @param productId 商品ID
     * @return 评价总数
     */
    int countByProductId(@Param("productId") Long productId);
    
    /**
     * 统计指定用户的评价总数
     * @param userId 用户ID
     * @return 评价总数
     */
    int countByUserId(@Param("userId") Long userId);
} 