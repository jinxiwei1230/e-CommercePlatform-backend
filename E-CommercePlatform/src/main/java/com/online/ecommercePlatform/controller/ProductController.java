package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.dto.ProductDTO;
import com.online.ecommercePlatform.dto.ProductQueryDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品控制器，处理产品相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService; // 产品服务，用于处理产品业务逻辑

    /**
     * 获取首页轮播展示的热门商品
     * @param limit 查询数量(可选，默认5)
     * @return Result包装的热门商品列表
     */
    @GetMapping("/hot")
    public Result<List<ProductBasicInfoDTO>> getHotProducts(
                    @RequestParam(required = false,
                    defaultValue = "5") int limit) {
        return productService.getHotProducts(limit);
    }

    /**
     * 获取热门类别及其热门商品API
     * @return 统一响应结果，包含4个热门类别及其各自5个热门商品
     */
    @GetMapping("/categories/hot")
    public Result<List<CategoryHotProductsDTO>> getHotCategoriesAndProducts() {
        return productService.getHotCategoriesAndProducts();
    }

    @GetMapping("/{categoryId}/products")
    public Result<PageBean<ProductDTO>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        try {
            // 参数校验
            if (categoryId == null || categoryId.trim().isEmpty()) {
                return Result.error(Result.BAD_REQUEST, "类别 ID 不能为空");
            }
            if (page < 1 || pageSize < 1) {
                return Result.error(Result.BAD_REQUEST, "分页参数无效");
            }
            if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                return Result.error(Result.BAD_REQUEST, "最低价格不能大于最高价格");
            }
            ProductQueryDTO queryDTO = new ProductQueryDTO();
            queryDTO.setCategoryId(categoryId);
            queryDTO.setPage(page);
            queryDTO.setPageSize(pageSize);
            queryDTO.setSortBy(sortBy);
            queryDTO.setSortOrder(sortOrder);
            queryDTO.setMinPrice(minPrice);
            queryDTO.setMaxPrice(maxPrice);

            List<ProductDTO> products = productService.getProductsByCategory(queryDTO);
            Long total = (long) productService.countProductsByCategory(queryDTO); // 注意类型转换

            PageBean<ProductDTO> pageBean = new PageBean<>();
            pageBean.setTotal(total);
            pageBean.setItems(products);
            return Result.success(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "服务器错误: " + e.getMessage());
        }
    }
}