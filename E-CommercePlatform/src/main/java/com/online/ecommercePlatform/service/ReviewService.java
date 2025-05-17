package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.ReviewDTO;
import com.online.ecommercePlatform.dto.ReviewResponseDTO;
import com.online.ecommercePlatform.dto.ReviewSimpleDTO;
import com.online.ecommercePlatform.dto.UnreviewedProductDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Review;

import java.util.List;

/**
 * 评价服务接口
 */
public interface ReviewService {

    /**
     * 获取用户已完成但未评价的订单商品列表
     * @param userId 用户ID
     * @return 未评价商品列表
     */
    List<UnreviewedProductDTO> getUnreviewedProducts(Long userId);

    /**
     * 用户提交商品评价
     * @param reviewDTO 评价信息
     * @param userId 用户ID
     * @return 评价ID
     */
    Long submitReview(ReviewDTO reviewDTO, Long userId);

    /**
     * 获取指定商品的评价列表
     * @param productId 商品ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价分页列表
     */
    PageBean<ReviewResponseDTO> getProductReviews(Long productId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户自己的评价列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价分页列表
     */
    PageBean<ReviewResponseDTO> getUserReviews(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户自己的评价ID列表（简化版，不分页）
     * @param userId 用户ID
     * @return 评价ID列表
     */
    List<ReviewSimpleDTO> getUserReviewIds(Long userId);

    /**
     * 修改用户已提交的评价
     * @param reviewId 评价ID
     * @param reviewDTO 更新的评价信息
     * @param userId 用户ID
     * @return 是否更新成功
     */
    boolean updateReview(Long reviewId, ReviewDTO reviewDTO, Long userId);

    /**
     * 删除用户已提交的评价
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteReview(Long reviewId, Long userId);

    /**
     * 获取评价的详细信息
     * @param reviewId 评价ID
     * @return 评价详情
     */
    ReviewResponseDTO getReviewDetails(Long reviewId);
} 