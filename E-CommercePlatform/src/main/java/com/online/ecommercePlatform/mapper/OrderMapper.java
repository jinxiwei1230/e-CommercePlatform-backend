package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.CheckoutItemDTO;
import com.online.ecommercePlatform.pojo.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Result;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {

    // 查询购物车商品信息
    @Select("SELECT c.cart_id, c.product_id, p.name, c.quantity, p.price AS unit_price, p.stock, p.freight " +
            "FROM Cart c JOIN Product p ON c.product_id = p.product_id " +
            "WHERE c.user_id = #{userId} AND c.cart_id IN (${cartIds})")
    @Results({
            @Result(property = "unitPrice", column = "unit_price", javaType = BigDecimal.class),
            @Result(property = "freight", column = "freight", javaType = BigDecimal.class)
    })
    List<CheckoutItemDTO> getCartItemsForCheckout(@Param("userId") Long userId, @Param("cartIds") String cartIds);

    // 查询用户的所有地址
    @Select("SELECT address_id, recipient_name, phone, address_detail, city, state, postal_code, is_default " +
            "FROM Address WHERE user_id = #{userId}")
    List<Address> getUserAddresses(@Param("userId") Long userId);

    // 查询用户可用的优惠券
    @Select("SELECT c.coupon_id, c.type, c.discount_value, c.min_order_amount " +
            "FROM Coupon c JOIN CouponDistribution cd ON c.coupon_id = cd.coupon_id " +
            "WHERE cd.user_id = #{userId} AND c.status = '未使用' " +
            "AND c.start_time <= NOW() AND c.end_time >= NOW() " +
            "AND c.min_order_amount <= #{totalAmount}")
    List<Coupon> getAvailableCoupons(@Param("userId") Long userId, @Param("totalAmount") BigDecimal totalAmount);


    // 插入订单
    @Insert("INSERT INTO `Order` (user_id, total_amount, status, payment_method, coupon_id, address_id, discount_amount, create_time) " +
            "VALUES (#{userId}, #{totalAmount}, #{status}, #{paymentMethod}, #{couponId}, #{addressId}, #{discountAmount}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void insertOrder(Order order);

    // 插入订单明细
    @Insert("INSERT INTO OrderDetail (order_id, product_id, quantity, unit_price, create_time) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{unitPrice}, NOW())")
    void insertOrderDetail(OrderDetail orderDetail);

    // 更新商品库存和销量
    @Update("UPDATE Product SET stock = stock - #{quantity}, sales = sales + #{quantity} " +
            "WHERE product_id = #{productId} AND stock >= #{quantity}")
    int updateProductStockAndSales(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    // 删除购物车商品
    @Delete("DELETE FROM Cart WHERE cart_id IN (${cartIds}) AND user_id = #{userId}")
    void deleteCartItems(@Param("userId") Long userId, @Param("cartIds") String cartIds);

    // 更新优惠券状态
    @Update("UPDATE Coupon SET status = '已使用', update_time = NOW() " +
            "WHERE coupon_id = #{couponId} AND status = '未使用'")
    int updateCouponStatus(@Param("couponId") Long couponId);

    // 查询订单状态
    @Select("SELECT status FROM `Order` WHERE order_id = #{orderId} AND user_id = #{userId}")
    String getOrderStatus(@Param("orderId") Long orderId, @Param("userId") Long userId);

    // 更新订单状态
    @Update("UPDATE `Order` SET status = #{status}, update_time = NOW() " +
            "WHERE order_id = #{orderId} AND user_id = #{userId}")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("userId") Long userId, @Param("status") String status);

    // 插入操作日志
    @Insert("INSERT INTO OperationLog (user_id, action, target_table, target_id, description, result, create_time) " +
            "VALUES (#{userId}, #{action}, #{targetTable}, #{targetId}, #{description}, #{result}, NOW())")
    void insertOperationLog(OperationLog log);
}