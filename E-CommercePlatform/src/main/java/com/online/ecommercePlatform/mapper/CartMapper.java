package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Cart;
import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 购物车数据访问接口
 * 定义购物车相关的数据库操作
 */
@Mapper
public interface CartMapper {
    /**
     * 检查商品是否已在购物车中
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购物车对象，不存在则返回null
     */
    Cart findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    
    /**
     * 更新购物车中商品数量
     * @param cartId 购物车ID
     * @param quantity 新数量
     * @return 受影响的行数
     */
    int updateQuantity(@Param("cartId") Long cartId, @Param("quantity") Integer quantity);

    /**
     * 根据用户ID查询用户购物车中的所有商品
     * @param userId 用户ID
     * @return 购物车商品列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询购物车项
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购物车项
     */
    Cart selectByUserIdAndProductId(Long userId, Long productId);

    /**
     * 新增购物车项
     * @param cart 购物车对象
     * @return 受影响的行数
     */
    int insert(Cart cart);

    /**
     * 更新购物车项数量
     * @param cart 购物车对象
     * @return 受影响的行数
     */
    int updateQuantity(Cart cart);

    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 受影响的行数
     */
    int deleteByUserId(Long userId);

    /**
     * 根据购物车ID查询购物车项
     * @param cartId 购物车ID
     * @return 购物车项
     */
    Cart selectByCartId(Long cartId);

    /**
     * 根据购物车ID删除购物车项
     * @param cartId 购物车ID
     * @return 影响的行数
     */
    int deleteByCartId(Long cartId);
}
