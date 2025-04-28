package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.CartAddDTO;
import com.online.ecommercePlatform.dto.CartAddResponseDTO;
import com.online.ecommercePlatform.dto.CartCheckoutDTO;
import com.online.ecommercePlatform.mapper.CartMapper;
import com.online.ecommercePlatform.mapper.ProductMapper;
import com.online.ecommercePlatform.pojo.Cart;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Override
    public List<Cart> selectByUserId(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    /**
     * 添加商品到购物车（如果已存在则增加数量，否则新增记录）
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 要添加的商品数量
     */
    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 检查是否已存在该商品
        Cart existingCart = cartMapper.selectByUserIdAndProductId(userId, productId);

        if (existingCart != null) {
            // 如果已存在，更新数量
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            cartMapper.updateQuantity(existingCart);
        } else {
            // 不存在，新增记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setCreateTime(LocalDateTime.now());
            cartMapper.insert(cart);
        }
    }

    /**
     * 更新购物车中指定商品的数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 新的商品数量
     */
    @Override
    @Transactional
    public Cart updateCartItem(Long userId, Long productId, Integer quantity) {
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new RuntimeException("购物车中未找到该商品");
        }
        cart.setQuantity(quantity);
        cartMapper.updateQuantity(cart);
        return cart;
    }

    /**
     * 从购物车中移除指定商品
     * @param userId 用户ID
     * @param productId 要移除的商品ID
     */
    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartMapper.deleteByUserIdAndProductId(userId, productId);
    }

    /**
     * 清空指定用户的购物车
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartMapper.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public CartCheckoutDTO checkout(Long userId) {
        // 1. 获取用户购物车列表
        List<Cart> cartItems = cartMapper.selectByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法结算");
        }

        // 2. 构建结算DTO
        CartCheckoutDTO checkoutDTO = new CartCheckoutDTO();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalFreight = BigDecimal.ZERO;

        // 3. 遍历购物车商品
        for (Cart cartItem : cartItems) {
            Product product = productMapper.findById(cartItem.getProductId());
            if (product == null) {
                throw new RuntimeException("商品ID:" + cartItem.getProductId() + "不存在");
            }

            // 检查库存
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品:" + product.getName() + "库存不足");
            }

            // 计算商品小计和运费
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            BigDecimal freight = BigDecimal.valueOf(product.getFreight());

            // 构建商品项DTO
            CartCheckoutDTO.CartItemDTO itemDTO = new CartCheckoutDTO.CartItemDTO();
            itemDTO.setProductId(product.getProductId());
            itemDTO.setProductName(product.getName());
            itemDTO.setQuantity(cartItem.getQuantity());
            itemDTO.setPrice(price);
            itemDTO.setSubtotal(subtotal);
            itemDTO.setFreight(freight);

            checkoutDTO.getItems().add(itemDTO);
            totalAmount = totalAmount.add(subtotal);
            totalFreight = totalFreight.add(freight);
        }

        // 4. 设置结算总金额
        checkoutDTO.setTotalAmount(totalAmount);
        checkoutDTO.setTotalFreight(totalFreight);
        checkoutDTO.setPaymentAmount(totalAmount.add(totalFreight));

        return checkoutDTO;
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
