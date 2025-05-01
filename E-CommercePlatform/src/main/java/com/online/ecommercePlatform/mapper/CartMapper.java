package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Cart;
import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {
//    查询用户购物车列表
    @Select("")
    List<Product> cartList();
    
    /**
     * 添加商品到购物车
     * @param cart 购物车对象
     * @return 受影响的行数
     */
    int addToCart(Cart cart);
    
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
    List<Cart> selectByUserId(Long userId);
    Cart selectByUserIdAndProductId(Long userId, Long productId);
    int insert(Cart cart);
    int updateQuantity(Cart cart);
    
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
