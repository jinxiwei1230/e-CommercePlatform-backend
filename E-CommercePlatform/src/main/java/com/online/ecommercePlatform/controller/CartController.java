package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CartAddDTO;
import com.online.ecommercePlatform.dto.CartAddResponseDTO;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取购物车列表
     * @return 购物车商品列表的Result对象
     */
    @GetMapping
    public Result<List<Product>> cartList() {
        List<Product> pageBean = cartService.cartList();
        return Result.success(pageBean);
    }

    /**
     * 添加商品到购物车
     * @param cartAddDTO 添加购物车请求对象
     * @return 添加结果的Result对象
     */
    @PostMapping("/add")
    public Result<CartAddResponseDTO> addToCart(@RequestBody @Valid CartAddDTO cartAddDTO) {
        try {
            // 用户ID需要从JWT token获取
            Long userId = 1L; // 假设用户ID为1

            // 调用服务添加到购物车
            return cartService.addToCart(userId, cartAddDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "添加购物车失败: " + e.getMessage());
        }
    }
}
