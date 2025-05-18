package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.pojo.Coupon;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CouponService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券控制器
 */
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 新增优惠券
     * @param coupon 优惠券对象
     * @return 创建结果
     */
    @PostMapping("/add")
    public Result<Coupon> add(@RequestBody Coupon coupon) {
        try {
            // 创建优惠券并自动分发
            Coupon createdCoupon = couponService.createCouponAndDistribute(coupon);
            return Result.success(createdCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改优惠券信息
     * @param coupon 更新后的优惠券对象
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<Coupon> update(@RequestBody Coupon coupon) {
        try {
            Coupon updatedCoupon = couponService.updateCoupon(coupon);
            return Result.success(updatedCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 查询结果
     */
    @GetMapping("/selectById/{id}")
    public Result<Coupon> selectById(@PathVariable Long id) {
        try {
            Coupon coupon = couponService.getCouponById(id);
            if (coupon != null) {
                return Result.success(coupon);
            } else {
                return Result.error("优惠券不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除优惠券
     * @param id 优惠券ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        try {
            boolean result = couponService.deleteCoupon(id);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除失败，优惠券可能不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除优惠券
     * @param ids 优惠券ID列表
     * @return 删除结果
     */
    @DeleteMapping("/delete/batch")
    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
        try {
            int count = couponService.deleteCouponBatch(ids);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有优惠券
     * @return 优惠券列表
     */
    @GetMapping("/selectAll")
    public Result<List<Coupon>> selectAll(Coupon coupon) {
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            return Result.success(coupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询优惠券
     * @param coupon 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping("/selectPage")
    public Result<PageBean<Coupon>> selectPage(
            Coupon coupon,
            @RequestParam(defaultValue = "1") int pageNum, 
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageBean<Coupon> pageBean = couponService.getCouponsByPage(pageNum, pageSize, coupon);
            return Result.success(pageBean);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询当前登录用户的优惠券（分页）
     * @param request HTTP请求对象
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 当前用户的优惠券分页结果
     */
    @GetMapping("/user/coupons")
    public Result<PageBean<Coupon>> getCurrentUserCoupons(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            // 获取当前登录用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error("用户未登录或登录已过期");
            }
            
            PageBean<Coupon> pageBean = couponService.getUserCouponsByPage(userId, pageNum, pageSize);
            return Result.success(pageBean);
        } catch (Exception e) {
            return Result.error(e.getMessage());
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
