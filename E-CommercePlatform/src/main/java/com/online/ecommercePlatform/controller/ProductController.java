package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品控制器，处理产品相关的 HTTP 请求
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService; // 产品服务，用于处理产品业务逻辑

    /**
     * 获取首页轮播展示的热门商品
     * @param limit 查询数量(可选，默认5)
     * @return Result包装的热门商品列表
     */
    @GetMapping("/products/hot")
    public Result<List<ProductBasicInfoDTO>> getHotProducts(
                    @RequestParam(required = false,
                    defaultValue = "5") int limit) {
        return productService.getHotProducts(limit);
    }

    /**
     * 获取热门类别及其热门商品API
     * @return 统一响应结果，包含4个热门类别及其各自5个热门商品
     */
    @GetMapping("/categories/products/hot")
    public Result<List<CategoryHotProductsDTO>> getHotCategoriesAndProducts() {
        return productService.getHotCategoriesAndProducts();
    }


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
                return Result.error(Result.NOT_FOUND, "未找到符合条件的产品");
            }
            
            return Result.success(products);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.BAD_REQUEST, "搜索产品时发生错误: " + e.getMessage());
        }
    }

}