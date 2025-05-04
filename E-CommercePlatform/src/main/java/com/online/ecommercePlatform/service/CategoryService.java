package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 返回分类列表
     * @param onlyNonLeaf true表示只获取非叶子节点
     * @return 分类树形列表
     */
    List<Category> getCategories(boolean onlyNonLeaf);

    // 创建子分类
    void createSubCategory(Long parentId, String name);

    // 删除分类并级联更新
    void deleteCategory(Long categoryId);

    // 修改分类名称
    void updateCategoryName(Long categoryId, String newName);

}
