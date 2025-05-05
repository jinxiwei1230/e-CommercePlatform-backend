package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.pojo.Category;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CategoryService;
import com.online.ecommercePlatform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/category/categories")
    public Result<List<Category>> getAllCategories(){

        // 调用服务层获取分类数据（true表示只获取非叶子节点）
        List<Category> cs = categoryService.getCategories(true);
        return Result.success(cs);
    }

    /**
     * 获取所有分类，包括叶子分类（包含子分类的树形结构）
     * GET /api/categories/tree
     * @return 统一响应结果包装，包含分类树形列表
     */
    @GetMapping("/categories/tree")
    public Result<List<Category>> getCategoriesTree(){

        // 调用服务层获取分类数据（false表示获取全部节点）
        List<Category> cs = categoryService.getCategories(false);
        return Result.success(cs);
    }

    /**
     * 创建子分类
     * POST /api/category/subcategory
     * @param parentId 父分类ID
     * @param name 子分类名称
     * @return 操作结果
     */
    @PostMapping("/category/subcategory")
    public Result<String> createSubCategory(@RequestParam Long parentId, @RequestParam String name) {
        try {
            categoryService.createSubCategory(parentId, name);
            return Result.success("子分类创建成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除分类
     * DELETE /api/category/{categoryId}
     * @param categoryId 要删除的分类ID
     * @return 操作结果
     */
    @DeleteMapping("/category/{categoryId}")
    public Result<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return Result.success("分类删除成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改分类名称
     * PUT /api/category/{categoryId}/name
     * @param categoryId 分类ID
     * @param newName 新名称
     * @return 操作结果
     */
    @PutMapping("/category/{categoryId}/name")
    public Result<String> updateCategoryName(@PathVariable Long categoryId, @RequestParam String newName) {
        try {
            categoryService.updateCategoryName(categoryId, newName);
            return Result.success("分类名称更新成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}
