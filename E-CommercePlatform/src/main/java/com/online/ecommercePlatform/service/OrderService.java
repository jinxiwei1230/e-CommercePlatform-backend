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

}