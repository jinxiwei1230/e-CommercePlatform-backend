package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.service.UserService;
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
     * @return 符合条件的产品列表
     */
    @GetMapping
    public List<Product> searchProducts(@RequestParam String keyword,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) Double minPrice,
                                        @RequestParam(required = false) Double maxPrice) {
        return productService.searchProducts(keyword, categoryId, minPrice, maxPrice);
    }

    /**
     * 根据产品 ID 获取产品信息
     * @param id 产品 ID
     * @return 对应的产品对象
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
