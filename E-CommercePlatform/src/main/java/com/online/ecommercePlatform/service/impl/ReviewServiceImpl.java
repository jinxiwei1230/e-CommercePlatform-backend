package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.ReviewDTO;
import com.online.ecommercePlatform.dto.ReviewResponseDTO;
import com.online.ecommercePlatform.dto.UnreviewedProductDTO;
import com.online.ecommercePlatform.mapper.OrderMapper;
import com.online.ecommercePlatform.mapper.ReviewMapper;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Review;
import com.online.ecommercePlatform.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评价服务实现类
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<UnreviewedProductDTO> getUnreviewedProducts(Long userId) {
        return reviewMapper.selectUnreviewedProducts(userId);
    }

    @Override
    @Transactional
    public Long submitReview(ReviewDTO reviewDTO, Long userId) {
        // 检查是否已评价过该订单商品
        int count = reviewMapper.checkReviewExists(userId, reviewDTO.getProductId(), reviewDTO.getOrderId());
        if (count > 0) {
            return null; // 已经评价过，返回空值表示失败
        }
        
        // 创建评价对象
        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(reviewDTO.getProductId());
        review.setOrderId(reviewDTO.getOrderId());
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());
        
        // 保存评价
        reviewMapper.insert(review);
        
        // 更新订单的评价状态为"已评价"
        orderMapper.updateReviewStatus(reviewDTO.getOrderId(), "已评价");
        
        return review.getReviewId();
    }

    @Override
    public PageBean<ReviewResponseDTO> getProductReviews(Long productId, Integer pageNum, Integer pageSize) {
        // 查询总记录数
        int total = reviewMapper.countByProductId(productId);
        
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<ReviewResponseDTO> page = (Page<ReviewResponseDTO>) reviewMapper.selectByProductId(productId);
        
        // 封装结果
        PageBean<ReviewResponseDTO> pageBean = new PageBean<>();
        pageBean.setTotal((long) total);
        pageBean.setItems(page.getResult());
        
        return pageBean;
    }

    @Override
    public PageBean<ReviewResponseDTO> getUserReviews(Long userId, Integer pageNum, Integer pageSize) {
        // 查询总记录数
        int total = reviewMapper.countByUserId(userId);
        
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<ReviewResponseDTO> page = (Page<ReviewResponseDTO>) reviewMapper.selectByUserId(userId);
        
        // 封装结果
        PageBean<ReviewResponseDTO> pageBean = new PageBean<>();
        pageBean.setTotal((long) total);
        pageBean.setItems(page.getResult());
        
        return pageBean;
    }

    @Override
    @Transactional
    public boolean updateReview(Long reviewId, ReviewDTO reviewDTO, Long userId) {
        // 检查评价是否存在且属于该用户
        Review existingReview = reviewMapper.checkReviewBelongsToUser(reviewId, userId);
        if (existingReview == null) {
            return false; // 评价不存在或不属于当前用户
        }
        
        // 更新评价
        existingReview.setRating(reviewDTO.getRating());
        existingReview.setContent(reviewDTO.getContent());
        
        return reviewMapper.update(existingReview) > 0;
    }

    @Override
    @Transactional
    public boolean deleteReview(Long reviewId, Long userId) {
        // 获取评价信息，用于后续更新订单评价状态
        Review review = reviewMapper.checkReviewBelongsToUser(reviewId, userId);
        if (review == null) {
            return false; // 评价不存在或不属于当前用户
        }
        
        // 删除评价
        boolean deleted = reviewMapper.delete(reviewId, userId) > 0;
        
        if (deleted) {
            // 检查该订单下是否还有其他评价
            int remainingReviews = reviewMapper.checkReviewExists(userId, review.getProductId(), review.getOrderId());
            
            // 如果没有其他评价，将订单评价状态更新为"未评价"
            if (remainingReviews == 0) {
                orderMapper.updateReviewStatus(review.getOrderId(), "未评价");
            }
        }
        
        return deleted;
    }

    @Override
    public ReviewResponseDTO getReviewDetails(Long reviewId) {
        return reviewMapper.selectById(reviewId);
    }
} 