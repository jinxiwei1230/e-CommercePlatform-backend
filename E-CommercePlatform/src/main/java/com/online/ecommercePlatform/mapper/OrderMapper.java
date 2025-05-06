package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.CheckoutItemDTO;
import com.online.ecommercePlatform.pojo.*;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {

    // 查询购物车商品信息
    List<CheckoutItemDTO> getCartItemsForCheckout(Long userId, String cartIds);

    // 查询用户的所有地址
    List<Address> getUserAddresses(Long userId);

    // 查询用户可用的优惠券
    List<Coupon> getAvailableCoupons(Long userId, BigDecimal totalAmount);

    // 插入订单
    void insertOrder(Order order);

    // 插入订单明细
    void insertOrderDetail(OrderDetail orderDetail);

    // 更新商品库存和销量
    int updateProductStockAndSales(Long productId, Integer quantity);

    // 删除购物车商品
    void deleteCartItems(Long userId, String cartIds);

    // 更新优惠券状态
    int updateCouponStatus(Long couponId);

    // 查询订单状态
    String getOrderStatus(Long orderId, Long userId);

    // 更新订单状态
    int updateOrderStatus(Long orderId, Long userId, String status);

    // 插入操作日志
    void insertOperationLog(OperationLog log);
}