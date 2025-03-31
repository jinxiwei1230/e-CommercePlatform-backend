package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.mapper.CategoryMapper;
import com.online.ecommercePlatform.pojo.Category;
import com.online.ecommercePlatform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取分类树形结构
     * @param onlyNonLeaf 是否只返回非叶子节点（有子分类的分类）
     * @return 分类树形结构列表（只包含根节点及其子节点层次结构）
     */
    @Override
    public List<Category> getCategories(boolean onlyNonLeaf) {
        // 从数据库获取所有分类的平面列表
        List<Category> categories = categoryMapper.alllist();

        // 如果分类列表为空，直接返回空列表
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }

        // 创建分类ID到分类对象的映射表，便于快速查找
        Map<Long, Category> categoryMap = createCategoryMap(categories);

        // 如果需要过滤叶子节点，则构建包含子节点的分类ID映射表
        Map<Long, Boolean> hasChildrenMap = onlyNonLeaf ?
                buildHasChildrenMap(categories) :
                new HashMap<>();

        // 构建并返回分类树形结构
        return buildCategoryTree(categoryMap, hasChildrenMap, onlyNonLeaf);
    }

    /**
     * 创建分类ID到分类对象的映射表
     * @param categories 分类列表
     * @return 分类ID映射表
     */
    private Map<Long, Category> createCategoryMap(List<Category> categories) {
        Map<Long, Category> categoryMap = new HashMap<>();

        // 遍历所有分类，创建新的分类对象放入映射表
        for (Category category : categories) {
            Category node = new Category();
            node.setCategoryId(category.getCategoryId());
            node.setName(category.getName());
            node.setParentId(category.getParentId());
            categoryMap.put(category.getCategoryId(), node);
        }

        return categoryMap;
    }

    /**
     * 构建包含子节点的分类ID映射表
     * @param categories 分类列表
     * @return 包含子节点的分类ID映射表（值为true表示该分类有子节点）
     */
    private Map<Long, Boolean> buildHasChildrenMap(List<Category> categories) {
        Map<Long, Boolean> hasChildrenMap = new HashMap<>();

        // 遍历所有分类，记录哪些分类被引用为父分类（即有子节点）
        for (Category category : categories) {
            Long parentId = category.getParentId();
            // 如果父ID有效（不为null且不为0），则标记该父分类有子节点
            if (parentId != null && parentId != 0) {
                hasChildrenMap.put(parentId, true);
            }
        }

        return hasChildrenMap;
    }

    /**
     * 构建分类树形结构
     * @param categoryMap 分类ID映射表
     * @param hasChildrenMap 包含子节点的分类ID映射表
     * @param onlyNonLeaf 是否只返回非叶子节点
     * @return 分类树形结构列表
     */
    private List<Category> buildCategoryTree(Map<Long, Category> categoryMap,
                                             Map<Long, Boolean> hasChildrenMap,
                                             boolean onlyNonLeaf) {
        List<Category> result = new ArrayList<>();

        // 遍历所有分类节点
        for (Category node : categoryMap.values()) {
            // 如果需要过滤叶子节点且当前节点是叶子节点，则跳过
            if (onlyNonLeaf && !hasChildrenMap.containsKey(node.getCategoryId())) {
                continue;
            }

            if (node.getParentId() == 0) {
                // 根节点直接添加到结果列表
                result.add(node);
            } else {
                // 非根节点，找到其父节点并添加到父节点的子节点列表
                Category parent = categoryMap.get(node.getParentId());
                if (parent != null) {
                    // 如果需要过滤叶子节点，确保父节点也是非叶子节点
                    if (!onlyNonLeaf || hasChildrenMap.containsKey(parent.getCategoryId())) {
                        initializeChildrenListIfNeeded(parent);
                        parent.getChildren().add(node);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 初始化分类的子节点列表（如果尚未初始化）
     * @param category 需要初始化子节点列表的分类
     */
    private void initializeChildrenListIfNeeded(Category category) {
        if (category.getChildren() == null) {
            category.setChildren(new ArrayList<>());
        }
    }
}