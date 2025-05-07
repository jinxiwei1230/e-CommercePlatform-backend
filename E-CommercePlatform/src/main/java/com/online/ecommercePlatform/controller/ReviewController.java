package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.ReviewDTO;
import com.online.ecommercePlatform.dto.ReviewResponseDTO;
import com.online.ecommercePlatform.dto.UnreviewedProductDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.service.ReviewService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取用户已完成但未评价的订单商品列表
     * @param request HTTP请求对象
     * @return 未评价商品列表
     */
    @GetMapping("/unreviewedProducts")
    public Result<List<UnreviewedProductDTO>> getUnreviewedProducts(HttpServletRequest request) {
        // 获取当前登录用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "用户未登录或登录已过期");
        }

        List<UnreviewedProductDTO> list = reviewService.getUnreviewedProducts(userId);
        return Result.success(list);
    }

    /**
     * 用户提交商品评价
     * @param reviewDTO 评价信息
     * @param request HTTP请求对象
     * @return 评价ID
     */
    @PostMapping
    public Result<Long> submitReview(
            HttpServletRequest request,
            @RequestBody @Validated ReviewDTO reviewDTO) {
        // 获取当前登录用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "用户未登录或登录已过期");
        }

        Long reviewId = reviewService.submitReview(reviewDTO, userId);
        if (reviewId == null) {
            return Result.error(Result.BAD_REQUEST, "您已经评价过该订单商品");
        }
        return Result.success(reviewId);
    }

    /**
     * 获取指定商品的评价列表
     * @param productId 商品ID
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @return 评价分页列表
     */
    @GetMapping("/product/{productId}")
    public Result<PageBean<ReviewResponseDTO>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageBean<ReviewResponseDTO> pageBean = reviewService.getProductReviews(productId, pageNum, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 获取用户自己的评价列表
     * @param request HTTP请求对象
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @return 评价分页列表
     */
    @GetMapping("/user")
    public Result<PageBean<ReviewResponseDTO>> getUserReviews(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        // 获取当前登录用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "用户未登录或登录已过期");
        }

        PageBean<ReviewResponseDTO> pageBean = reviewService.getUserReviews(userId, pageNum, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 修改用户已提交的评价
     * @param request HTTP请求对象
     * @param reviewId 评价ID
     * @param reviewDTO 更新的评价信息
     * @return 成功/失败消息
     */
    @PutMapping("/{reviewId}")
    public Result<String> updateReview(
            HttpServletRequest request,
            @PathVariable Long reviewId,
            @RequestBody @Validated ReviewDTO reviewDTO) {

        // 获取当前登录用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "用户未登录或登录已过期");
        }

        boolean success = reviewService.updateReview(reviewId, reviewDTO, userId);
        if (success) {
            return Result.success("评价修改成功");
        } else {
            return Result.error(Result.NOT_FOUND, "评价不存在或不属于当前用户");
        }
    }

    /**
     * 删除用户已提交的评价
     * @param request HTTP请求对象
     * @param reviewId 评价ID
     * @return 成功/失败消息
     */
    @DeleteMapping("/{reviewId}")
    public Result<String> deleteReview(
            HttpServletRequest request,
            @PathVariable Long reviewId) {

        // 获取当前登录用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "用户未登录或登录已过期");
        }

        boolean success = reviewService.deleteReview(reviewId, userId);
        if (success) {
            return Result.success("评价删除成功");
        } else {
            return Result.error(Result.NOT_FOUND, "评价不存在或不属于当前用户");
        }
    }

    /**
     * 获取评价的详细信息
     * @param reviewId 评价ID
     * @return 评价详情
     */
    @GetMapping("/{reviewId}")
    public Result<ReviewResponseDTO> getReviewDetails(@PathVariable Long reviewId) {
        ReviewResponseDTO review = reviewService.getReviewDetails(reviewId);
        if (review != null) {
            return Result.success(review);
        } else {
            return Result.error(Result.NOT_FOUND, "评价不存在");
        }
    }

    /**
     * 从请求头中解析并验证token，获取用户ID
     * @param request HTTP请求对象
     * @return 用户ID，如果验证失败则返回null
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null || jwtUtil.isTokenExpired(token)) {
                return null;
            }

            String userIdStr = jwt.getClaim("userId").asString();
            return StringUtils.hasText(userIdStr) ? Long.valueOf(userIdStr) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
