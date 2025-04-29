package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.CartAddDTO;
import com.online.ecommercePlatform.dto.CartAddResponseDTO;
import com.online.ecommercePlatform.mapper.CartMapper;
import com.online.ecommercePlatform.mapper.ProductMapper;
import com.online.ecommercePlatform.pojo.Cart;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ProductMapper productMapper;

    //查询用户购物车列表
    @Override
    public List<Product> cartList() {
        return cartMapper.cartList();
    }
    
    /**
     * 将商品添加到购物车
     * @param userId 用户ID
     * @param cartAddDTO 添加购物车请求DTO
     * @return 添加购物车响应结果
     */
    @Override
    @Transactional
    public Result<CartAddResponseDTO> addToCart(Long userId, CartAddDTO cartAddDTO) {
        try {
            // 1. 验证商品是否存在
            Long productId = Long.valueOf(cartAddDTO.getProduct_id());
            Product product = productMapper.findById(productId);
            if (product == null) {
                return Result.error(Result.NOT_FOUND, "商品不存在");
            }
            
            // 2. 验证商品库存是否足够
            if (product.getStock() < cartAddDTO.getQuantity()) {
                return Result.error(Result.BAD_REQUEST, "商品库存不足");
            }
            
            // 3. 检查该商品是否已在购物车中
            Cart existingCart = cartMapper.findByUserIdAndProductId(userId, productId);
            
            Cart cart;
            if (existingCart != null) {
                // 4a. 如果已在购物车中，更新数量
                int newQuantity = existingCart.getQuantity() + cartAddDTO.getQuantity();
                
                // 再次验证库存
                if (product.getStock() < newQuantity) {
                    return Result.error(Result.BAD_REQUEST, "商品库存不足");
                }
                
                cartMapper.updateQuantity(existingCart.getCartId(), newQuantity);
                cart = existingCart;
            } else {
                // 4b. 如果不在购物车中，添加新记录
                cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setQuantity(cartAddDTO.getQuantity());
                cart.setCreateTime(LocalDateTime.now());
                
                cartMapper.addToCart(cart);
            }
            
            // 5. 返回成功响应
            CartAddResponseDTO responseDTO = new CartAddResponseDTO();
            responseDTO.setCart_id(cart.getCartId().toString());
            
            return Result.success(responseDTO);
            
        } catch (NumberFormatException e) {
            return Result.error(Result.BAD_REQUEST, "商品ID格式不正确");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "添加购物车失败: " + e.getMessage());
        }
    }
}
