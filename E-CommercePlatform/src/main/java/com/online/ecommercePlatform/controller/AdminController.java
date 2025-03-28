package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.service.UserService;
import com.online.ecommercePlatform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器，处理管理员对产品的管理操作
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ProductService productService; // 产品服务，用于处理产品业务逻辑

    /**
     * 添加新产品
     * @param product 产品对象
     * @return 添加成功的产品对象
     */
    @PostMapping("/product")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /**
     * 更新产品信息
     * @param id 产品 ID
     * @param product 更新后的产品对象
     * @return 更新后的产品对象
     */
    @PutMapping("/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setProductId(id);
        return productService.updateProduct(product);
    }

    /**
     * 删除产品
     * @param id 产品 ID
     */
    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
