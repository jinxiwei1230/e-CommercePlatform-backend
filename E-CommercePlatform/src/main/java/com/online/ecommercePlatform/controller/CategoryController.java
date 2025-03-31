package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.Category;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类控制器
 * 处理与商品分类相关的HTTP请求
 */
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有非叶子分类（包含子分类的树形结构）
     * GET /api/categories
     * @return 统一响应结果包装，包含分类树形列表
     */
    @GetMapping("/categories")
    public Result<List<Category>> getAllCategories(){

        // 调用服务层获取分类数据（true表示只获取非叶子节点）
        List<Category> cs = categoryService.getCategories(true);
        return Result.success(cs);
    }

}
