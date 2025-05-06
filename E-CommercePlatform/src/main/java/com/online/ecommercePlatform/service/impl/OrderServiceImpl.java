package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.mapper.OrderMapper;
import com.online.ecommercePlatform.pojo.*;
import com.online.ecommercePlatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public CheckoutResponseDTO checkout(Long userId, CheckoutRequestDTO request) {
        List<Long> cartIds = request.getCartIds();
        String cartIdsStr = cartIds.isEmpty() ? "SELECT cart_id FROM Cart WHERE user_id = " + userId :
                cartIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        // 查询购物车商品信息
        List<CheckoutItemDTO> checkoutItems = orderMapper.getCartItemsForCheckout(userId, cartIdsStr);
        if (checkoutItems.isEmpty()) {
            throw new RuntimeException("购物车商品不存在");
        }

        // 检查库存
        for (CheckoutItemDTO item : checkoutItems) {
            if (item.getQuantity() > item.getStock()) {
                throw new RuntimeException("商品 " + item.getName() + " 库存不足");
            }
        }

        // 计算总金额和运费
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal freight = BigDecimal.ZERO;
        for (CheckoutItemDTO item : checkoutItems) {
            BigDecimal subtotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubtotal(subtotal);
            totalAmount = totalAmount.add(subtotal);
            freight = freight.add(item.getFreight() != null ? item.getFreight() : BigDecimal.ZERO);
        }

        // 查询用户地址
        List<Address> addresses = orderMapper.getUserAddresses(userId);
        if (addresses.isEmpty()) {
            throw new RuntimeException("用户尚未添加地址");
        }

        // 查询可用优惠券
        List<Coupon> availableCoupons = orderMapper.getAvailableCoupons(userId, totalAmount);

        // 构建响应
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setCheckoutItems(checkoutItems);
        response.setTotalAmount(totalAmount);
        response.setFreight(freight);
        response.setAddresses(addresses);
        response.setAvailableCoupons(availableCoupons);
        return response;
    }

    @Override
    @Transactional
    public OrderCreateResponseDTO createOrder(Long userId, OrderCreateRequestDTO request) {
        List<Long> cartIds = request.getCartIds();
        String cartIdsStr = cartIds.isEmpty() ? "SELECT cart_id FROM Cart WHERE user_id = " + userId :
                cartIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        // 查询购物车商品
        List<CheckoutItemDTO> checkoutItems = orderMapper.getCartItemsForCheckout(userId, cartIdsStr);
        if (checkoutItems.isEmpty()) {
            throw new RuntimeException("购物车商品不存在");
        }

        // 检查库存并更新
        for (CheckoutItemDTO item : checkoutItems) {
            int updated = orderMapper.updateProductStockAndSales(item.getProductId(), item.getQuantity());
            if (updated == 0) {
                throw new RuntimeException("商品 " + item.getName() + " 库存不足");
            }
        }

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CheckoutItemDTO item : checkoutItems) {
            totalAmount = totalAmount.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        // 应用优惠券
        BigDecimal discountAmount = BigDecimal.ZERO;
        Long couponId = request.getCouponId();
        if (couponId != null) {
            List<Coupon> coupons = orderMapper.getAvailableCoupons(userId, totalAmount);
            Coupon selectedCoupon = coupons.stream()
                    .filter(c -> c.getCouponId().equals(couponId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("优惠券不可用"));
            if ("满减".equals(selectedCoupon.getType())) {
                discountAmount = new BigDecimal(selectedCoupon.getDiscountValue().toString());
            } else if ("折扣".equals(selectedCoupon.getType())) {
                BigDecimal discountValue = new BigDecimal(selectedCoupon.getDiscountValue().toString());
                discountAmount = totalAmount.multiply(discountValue.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
            } else if ("固定金额".equals(selectedCoupon.getType())) {
                discountAmount = new BigDecimal(selectedCoupon.getDiscountValue().toString());
            }
            totalAmount = totalAmount.subtract(discountAmount);
            orderMapper.updateCouponStatus(couponId);
        }
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("待支付");
        order.setPaymentMethod(request.getPaymentMethod());
        order.setCouponId(couponId);
        order.setAddressId(request.getAddressId()); // 设置 addressId
        order.setDiscountAmount(discountAmount);
        order.setPaymentUrl("https://payment.example.com/pay?order_id=" + order.getOrderId());

        System.out.println(order.toString());
        orderMapper.insertOrder(order);
        // 创建订单明细
        for (CheckoutItemDTO item : checkoutItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getOrderId());
            detail.setProductId(item.getProductId());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(item.getUnitPrice());
            orderMapper.insertOrderDetail(detail);
        }

        // 删除购物车商品
        orderMapper.deleteCartItems(userId, cartIdsStr);

        // 记录操作日志
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setAction("创建订单");
        log.setTargetTable("Order");
        log.setTargetId(order.getOrderId());
        log.setDescription("用户创建订单，订单ID: " + order.getOrderId());
        log.setResult("成功");
        orderMapper.insertOperationLog(log);

        // 构建响应
        OrderCreateResponseDTO response = new OrderCreateResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setTotalAmount(totalAmount);
        response.setDiscountAmount(discountAmount);
        response.setPaymentUrl("https://payment.example.com/pay?order_id=" + order.getOrderId());
        response.setStatus("待支付");
        return response;
    }

    @Override
    public PaymentInitiateResponseDTO initiatePayment(Long userId, Long orderId, PaymentInitiateRequestDTO request) {
        String status = orderMapper.getOrderStatus(orderId, userId);
        if (status == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"待支付".equals(status)) {
            throw new RuntimeException("订单状态不支持支付");
        }

        // 假设调用第三方支付网关API
        String paymentUrl = "https://payment.example.com/pay?order_id=" + orderId;
        String qrCode = "https://payment.example.com/qr?order_id=" + orderId;

        PaymentInitiateResponseDTO response = new PaymentInitiateResponseDTO();
        response.setOrderId(orderId);
        response.setPaymentUrl(paymentUrl);
        response.setQrCode(qrCode);
        return response;
    }

    @Override
    @Transactional
    public PaymentConfirmResponseDTO confirmPayment(Long userId, Long orderId, PaymentConfirmRequestDTO request) {
        String status = orderMapper.getOrderStatus(orderId, userId);
        if (status == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"待支付".equals(status)) {
            throw new RuntimeException("订单状态不支持支付确认");
        }

        // 假设验证支付网关返回的交易ID
        String transactionId = request.getTransactionId();
        if (!isValidTransaction(transactionId)) {
            throw new RuntimeException("支付验证失败");
        }

        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, userId, "已支付");

        // 记录操作日志
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setAction("支付确认");
        log.setTargetTable("Order");
        log.setTargetId(orderId);
        log.setDescription("用户确认支付，订单ID: " + orderId);
        log.setResult("成功");
        orderMapper.insertOperationLog(log);

        PaymentConfirmResponseDTO response = new PaymentConfirmResponseDTO();
        response.setOrderId(orderId);
        response.setStatus("已支付");
        return response;
    }

    @Override
    public PaymentStatusResponseDTO getPaymentStatus(Long userId, Long orderId) {
        Order order = orderMapper.getOrder(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        PaymentStatusResponseDTO response = new PaymentStatusResponseDTO();
        response.setStatus(order.getStatus());
        response.setPaymentTime(Timestamp.valueOf(order.getUpdateTime()));
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentAmount(order.getTotalAmount());
        return response;
    }

    @Override
    public OrderListResponseDTO getUserOrders(Long userId, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OrderSummaryDTO> orders = orderMapper.getUserOrders(userId, status, offset, pageSize);
        Long total = orderMapper.getUserOrdersCount(userId, status);

        for (OrderSummaryDTO order : orders) {
            List<ProductPreviewDTO> productPreview = orderMapper.getOrderProductPreview(order.getOrderId());
            order.setProductPreview(productPreview);
        }

        OrderListResponseDTO response = new OrderListResponseDTO();
        response.setTotal(total);
        response.setItems(orders);
        return response;
    }

    @Override
    public OrderDetailResponseDTO getOrderDetail(Long userId, Long orderId) {
        OrderDetailDTO order = orderMapper.getOrderDetail(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<OrderItemDTO> items = orderMapper.getOrderItems(orderId);
        Address address = orderMapper.getOrderAddress(orderId);
        Coupon coupon = orderMapper.getOrderCoupon(orderId);

        OrderDetailResponseDTO response = new OrderDetailResponseDTO();
        response.setOrder(order);
        response.setItems(items);
        response.setAddress(address);
        response.setCoupon(coupon);
        return response;
    }

    @Override
    @Transactional
    public OrderCancelResponseDTO cancelOrder(Long userId, Long orderId) {
        String currentStatus = orderMapper.getOrderStatus(orderId, userId);
        if (currentStatus == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"待支付".equals(currentStatus)) {
            throw new RuntimeException("订单状态不支持取消");
        }

        // 恢复库存
        List<OrderDetail> details = orderMapper.getOrderDetailsByOrderId(orderId);
        for (OrderDetail detail : details) {
            orderMapper.restoreProductStockAndSales(detail.getProductId(), detail.getQuantity());
        }

        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, userId, "已取消");

        OrderCancelResponseDTO response = new OrderCancelResponseDTO();
        response.setOrderId(orderId);
        response.setStatus("已取消");
        return response;
    }

    @Override
    @Transactional
    public OrderCancelResponseDTO confirmReceipt(Long userId, Long orderId) {
        String currentStatus = orderMapper.getOrderStatus(orderId, userId);
        if (currentStatus == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"已发货".equals(currentStatus)) {
            throw new RuntimeException("订单状态不支持确认收货");
        }

        orderMapper.updateOrderStatus(orderId, userId, "已完成");

        OrderCancelResponseDTO response = new OrderCancelResponseDTO();
        response.setOrderId(orderId);
        response.setStatus("已完成");
        return response;
    }

    @Override
    @Transactional
    public RefundResponseDTO applyRefund(Long userId, Long orderId, RefundRequestDTO refundRequest) {
        Order order = orderMapper.getOrder(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"已支付".equals(order.getStatus()) && !"已发货".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不支持退款");
        }

        // 创建退款申请
        Refund refund = new Refund();
        refund.setOrderId(orderId);
        refund.setUserId(userId);
        refund.setReason(refundRequest.getReason());
        refund.setRefundAmount(refundRequest.getRefundAmount());
        refund.setType(refundRequest.getType());
        orderMapper.insertRefund(refund);

        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, userId, "待退款");

        RefundResponseDTO response = new RefundResponseDTO();
        response.setRefundId(refund.getRefundId());
        response.setStatus("处理中");
        return response;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long adminUserId, Long orderId, String status) {
        // 通过 orderId 查询订单
        Order order = orderMapper.getOrder(orderId, null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        Long orderUserId = order.getUserId();

        // 验证状态是否合法
        String[] validStatuses = {"待支付", "已支付", "已发货", "已完成", "已退款", "待退款", "已取消"};
        boolean isValidStatus = Arrays.stream(validStatuses).anyMatch(s -> s.equals(status));
        if (!isValidStatus) {
            throw new RuntimeException("无效的订单状态");
        }

        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, orderUserId, status);

        // 再次查询更新后的订单信息
        Order updatedOrder = orderMapper.getOrder(orderId, orderUserId);
        if (updatedOrder == null) {
            throw new RuntimeException("订单更新失败");
        }

        // 记录操作日志，使用管理员 ID
        OperationLog log = new OperationLog();
        log.setUserId(adminUserId);
        log.setAction("管理员修改订单状态");
        log.setTargetTable("Order");
        log.setTargetId(orderId);
        log.setDescription("管理员将订单状态修改为 " + status + "，订单ID: " + orderId + "，下单用户ID: " + orderUserId);
        log.setResult("成功");
        orderMapper.insertOperationLog(log);

        return updatedOrder;
    }

    @Override
    public String getRoleByUserId(Long userId) {
        return orderMapper.getRoleByUserId(userId);
    }

    // 模拟支付网关验证
    private boolean isValidTransaction(String transactionId) {
        return transactionId != null && transactionId.startsWith("tx");
    }
}