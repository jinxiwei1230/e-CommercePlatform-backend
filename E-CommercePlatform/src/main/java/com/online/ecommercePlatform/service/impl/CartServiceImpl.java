package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.mapper.CartMapper;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    //查询用户购物车列表
    @Override
    public List<Product> cartList() {
        return cartMapper.cartList();
    }
}
