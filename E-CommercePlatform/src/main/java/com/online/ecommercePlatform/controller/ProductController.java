package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品控制器，处理产品相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService; // 产品服务，用于处理产品业务逻辑

    /**
     * 搜索产品
     * @param keyword 关键字
     * @param categoryId 类别 ID，可选
     * @param minPrice 最低价格，可选
     * @param maxPrice 最高价格，可选
     * @return 封装后的产品列表查询结果
     */
    @GetMapping
    public Result<List<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        try {
            List<Product> products = productService.searchProducts(keyword, categoryId, minPrice, maxPrice);
            if (products.isEmpty()) {
                return Result.error("未找到符合条件的产品");
            }
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("搜索产品时发生错误: " + e.getMessage());
        }
    }

    /**
     * 根据产品 ID 获取产品信息
     * @param id 产品 ID
     * @return 封装后的产品查询结果
     */
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Result.error("未找到ID为 " + id + " 的产品");
            }
            return Result.success(product);
        } catch (Exception e) {
            return Result.error("获取产品信息时发生错误: " + e.getMessage());
        }
    }
}