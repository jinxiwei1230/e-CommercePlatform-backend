package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.pojo.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    // 购物车结算
    CheckoutResponseDTO checkout(Long userId, CheckoutRequestDTO request);

    // 创建订单
    OrderCreateResponseDTO createOrder(Long userId, OrderCreateRequestDTO request);

    // 发起支付
    PaymentInitiateResponseDTO initiatePayment(Long userId, Long orderId, PaymentInitiateRequestDTO request);

    // 确认支付
    PaymentConfirmResponseDTO confirmPayment(Long userId, Long orderId, PaymentConfirmRequestDTO request);

    /**
     * 查询支付状态
     */
    PaymentStatusResponseDTO getPaymentStatus(Long userId, Long orderId);

    /**
     * 查询用户订单列表
     */
    OrderListResponseDTO getUserOrders(Long userId, String status, int page, int pageSize);

    /**
     * 查询订单详情
     */
    OrderDetailResponseDTO getOrderDetail(Long userId, Long orderId);

    /**
     * 取消订单
     */
    OrderCancelResponseDTO cancelOrder(Long userId, Long orderId);

    /**
     * 确认收货
     */
    OrderCancelResponseDTO confirmReceipt(Long userId, Long orderId);

    /**
     * 申请退款
     */
    RefundResponseDTO applyRefund(Long userId, Long orderId, RefundRequestDTO refundRequest);

    /**
     * 管理员修改订单状态
     */
    Order updateOrderStatus(Long adminUserId, Long orderId, String status);

    /**
     * 根据用户ID查询用户角色
     */
    String getRoleByUserId(Long userId);
}