package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.CheckoutItemDTO;
import com.online.ecommercePlatform.dto.OrderSummaryDTO;
import com.online.ecommercePlatform.dto.OrderItemDTO;
import com.online.ecommercePlatform.dto.ProductPreviewDTO;
import com.online.ecommercePlatform.dto.OrderDetailDTO;
import com.online.ecommercePlatform.pojo.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 查询购物车商品信息
     */
    List<CheckoutItemDTO> getCartItemsForCheckout(@Param("userId") Long userId, @Param("cartIds") String cartIds);

    /**
     * 查询用户的所有地址
     */
    List<Address> getUserAddresses(@Param("userId") Long userId);

    /**
     * 查询用户可用的优惠券
     */
    List<Coupon> getAvailableCoupons(@Param("userId") Long userId, @Param("totalAmount") BigDecimal totalAmount);

    /**
     * 查询用户名
     */
    String getUsernameByUserId(@Param("userId") Long userId);

    /**
     * 插入订单
     */
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int insertOrder(Order order);

    /**
     * 查询订单创建时间
     */
    Timestamp getOrderCreateTime(@Param("orderId") Long orderId);

    /**
     * 插入订单明细
     */
    int insertOrderDetail(OrderDetail orderDetail);

    /**
     * 更新商品库存和销量
     */
    int updateProductStockAndSales(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 恢复商品库存和销量
     */
    int restoreProductStockAndSales(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 删除购物车商品
     */
    int deleteCartItems(@Param("userId") Long userId, @Param("cartIds") String cartIds);

    /**
     * 更新优惠券状态
     */
    int updateCouponStatus(@Param("couponId") Long couponId);

    /**
     * 查询订单状态
     */
    String getOrderStatus(@Param("orderId") Long orderId, @Param("userId") Long userId);

    /**
     * 查询订单信息
     */
    Order getOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    /**
     * 更新订单支付信息
     */
    int updateOrderPaymentDetails(Order order);

    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("userId") Long userId, @Param("status") String status);

    /**
     * 查询订单明细（用于取消订单和退款时恢复库存）
     */
    List<OrderDetail> getOrderDetailsByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询用户订单列表
     */
    List<OrderSummaryDTO> getUserOrders(@Param("userId") Long userId, @Param("status") String status, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    /**
     * 查询订单总数
     */
    Long getUserOrdersCount(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 查询订单商品预览
     */
    List<ProductPreviewDTO> getOrderProductPreview(@Param("orderId") Long orderId);

    /**
     * 查询订单详情
     */
    OrderDetailDTO getOrderDetail(@Param("orderId") Long orderId, @Param("userId") Long userId);

    /**
     * 查询订单商品详情
     */
    List<OrderItemDTO> getOrderItems(@Param("orderId") Long orderId);

    /**
     * 查询订单地址
     */
    Address getOrderAddress(@Param("orderId") Long orderId);

    /**
     * 查询订单优惠券
     */
    Coupon getOrderCoupon(@Param("orderId") Long orderId);

    /**
     * 插入退款申请
     */
    @Options(useGeneratedKeys = true, keyProperty = "refundId")
    int insertRefund(Refund refund);

    /**
     * 插入操作日志
     */
    int insertOperationLog(OperationLog operationLog);

    /**
     * 查询用户角色
     */
    String getRoleByUserId(@Param("userId") Long userId);
}