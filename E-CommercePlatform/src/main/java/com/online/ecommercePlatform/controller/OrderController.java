package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.pojo.Order;
import com.online.ecommercePlatform.service.OrderService;
import com.online.ecommercePlatform.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 购物车结算
     */
    @PostMapping("/cart/checkout")
    public Result<CheckoutResponseDTO> checkout(HttpServletRequest request, @RequestBody CheckoutRequestDTO checkoutRequest) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }
        System.out.println("-------userId--------");
        System.out.println(userId);

        try {
            CheckoutResponseDTO response = orderService.checkout(userId, checkoutRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 创建订单
     */
    @PostMapping("/order/create")
    public Result<OrderCreateResponseDTO> createOrder(HttpServletRequest request, @RequestBody OrderCreateRequestDTO orderRequest) {
        Long userId = getUserIdFromRequest(request);
        System.out.println("-------userId--------");
        System.out.println(userId);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }
        System.out.println("-------userId--------");
        System.out.println(userId);
        try {
            OrderCreateResponseDTO response = orderService.createOrder(userId, orderRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 发起支付
     */
    @PostMapping("/order/{orderId}/payment/initiate")
    public Result<PaymentInitiateResponseDTO> initiatePayment(HttpServletRequest request,
                                                              @PathVariable Long orderId,
                                                              @RequestBody PaymentInitiateRequestDTO paymentRequest) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            PaymentInitiateResponseDTO response = orderService.initiatePayment(userId, orderId, paymentRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 确认支付
     */
    @PostMapping("/order/{orderId}/payment/confirm")
    public Result<PaymentConfirmResponseDTO> confirmPayment(HttpServletRequest request,
                                                            @PathVariable Long orderId,
                                                            @RequestBody PaymentConfirmRequestDTO confirmRequest) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            PaymentConfirmResponseDTO response = orderService.confirmPayment(userId, orderId, confirmRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 从请求头中解析并验证token，获取用户ID
     * @param request HTTP请求对象
     * @return 用户ID，如果验证失败则返回null
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
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