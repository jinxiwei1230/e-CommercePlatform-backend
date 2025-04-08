package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<List<Product>> cartList()
    {
        List<Product> pageBean = cartService.cartList();
        return Result.success(pageBean);

    }


}
