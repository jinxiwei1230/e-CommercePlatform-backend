package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.CartAddDTO;
import com.online.ecommercePlatform.dto.CartAddResponseDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;

import java.util.List;

public interface CartService {

    //查询用户购物车列表
    List<Product> cartList();
    
    /**
     * 将商品添加到购物车
     * @param userId 用户ID
     * @param cartAddDTO 添加购物车请求DTO
     * @return 添加购物车响应结果
     */
    Result<CartAddResponseDTO> addToCart(Long userId, CartAddDTO cartAddDTO);
}
