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
    @PostMapping("/create")
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
     * 查询支付状态
     */
    @GetMapping("/{orderId}/payment/status")
    public Result<PaymentStatusResponseDTO> getPaymentStatus(HttpServletRequest request,
                                                             @PathVariable Long orderId) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            PaymentStatusResponseDTO response = orderService.getPaymentStatus(userId, orderId);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public Result<OrderListResponseDTO> getUserOrders(HttpServletRequest request,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            OrderListResponseDTO response = orderService.getUserOrders(userId, status, page, pageSize);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderDetailResponseDTO> getOrderDetail(HttpServletRequest request,
                                                         @PathVariable Long orderId) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            OrderDetailResponseDTO response = orderService.getOrderDetail(userId, orderId);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public Result<OrderCancelResponseDTO> cancelOrder(HttpServletRequest request,
                                                      @PathVariable Long orderId) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            OrderCancelResponseDTO response = orderService.cancelOrder(userId, orderId);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 确认收货
     */
    @PostMapping("/{orderId}/confirm")
    public Result<OrderCancelResponseDTO> confirmReceipt(HttpServletRequest request,
                                                         @PathVariable Long orderId) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            OrderCancelResponseDTO response = orderService.confirmReceipt(userId, orderId);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 申请退款
     */
    @PostMapping("/{orderId}/refund")
    public Result<RefundResponseDTO> applyRefund(HttpServletRequest request,
                                                 @PathVariable Long orderId,
                                                 @RequestBody RefundRequestDTO refundRequest) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        try {
            RefundResponseDTO response = orderService.applyRefund(userId, orderId, refundRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(Result.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 管理员修改订单状态
     */
    @PutMapping("/{orderId}/admin/status")
    public Result<Order> updateOrderStatus(HttpServletRequest request,
                                           @PathVariable Long orderId,
                                           @RequestBody UpdateOrderStatusRequestDTO requestDTO) {
        Long adminUserId = getUserIdFromRequest(request);
        if (adminUserId == null) {
            return Result.error(Result.UNAUTHORIZED, "未授权");
        }

        // 验证管理员权限
        String role = getRoleFromRequest(request);
        if (!"管理员".equals(role)) {
            return Result.error(Result.FORBIDDEN, "权限不足");
        }

        try {
            Order updatedOrder = orderService.updateOrderStatus(adminUserId, orderId, requestDTO.getStatus());
            return Result.success(updatedOrder);
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

    /**
     * 从请求头中解析用户角色
     */
    private String getRoleFromRequest(HttpServletRequest request) {
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
            if (!StringUtils.hasText(userIdStr)) {
                return null;
            }

            Long userId = Long.valueOf(userIdStr);
            // 通过 Service 查询角色
            return orderService.getRoleByUserId(userId);
        } catch (Exception e) {
            return null;
        }
    }
}