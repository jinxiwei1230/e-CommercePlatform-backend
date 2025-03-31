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

}
