package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.pojo.Cart;

import java.util.List;
/**
 * 购物车服务接口，定义购物车相关操作
 */
public interface CartService {

    /**
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 商品数量
     */
    Cart addToCart(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车中商品的数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 新的商品数量
     */
    Cart updateCartItem(Long userId, Long productId, Integer quantity);

    /**
     * 从购物车中移除指定商品
     * @param userId 用户ID
     * @param cartId 要移除的购物车项ID
     */
    void removeFromCart(Long userId, Long cartId);

    /**
     * 清空指定用户的购物车
     * @param userId 用户ID
     */
    void clearCart(Long userId);

}
