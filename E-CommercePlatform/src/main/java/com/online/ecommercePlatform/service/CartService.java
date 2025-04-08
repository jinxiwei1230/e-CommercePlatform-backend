package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Product;

import java.util.List;

public interface CartService {

    //查询用户购物车列表
    List<Product> cartList();
}
