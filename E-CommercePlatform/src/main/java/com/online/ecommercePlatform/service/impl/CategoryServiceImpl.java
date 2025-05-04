package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.mapper.CategoryMapper;
import com.online.ecommercePlatform.pojo.Category;
import com.online.ecommercePlatform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
     * 创建子分类
     * @param parentId 父分类ID
     * @param name 子分类名称
     */
    @Override
    @Transactional
    public void createSubCategory(Long parentId, String name) {
        // 验证父分类是否存在
        Category parent = categoryMapper.findById(parentId);
        if (parent == null) {
            throw new IllegalArgumentException("父分类不存在");
        }

        // 创建子分类
        Category subCategory = new Category();
        subCategory.setName(name);
        subCategory.setParentId(parentId);
        subCategory.setCreateTime(LocalDateTime.now());
        categoryMapper.insert(subCategory);
    }

    /**
     * 删除分类并级联更新
     * @param categoryId 要删除的分类ID
     */
    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        // 获取分类信息
        Category category = categoryMapper.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }

        // 获取所有子分类ID（包括自身）
        List<Long> descendantIds = categoryMapper.findAllDescendantIds(categoryId);

        // 将商品的分类ID更新为父分类ID
        Long parentId = category.getParentId() != null && category.getParentId() != 0 ? category.getParentId() : 0L;
        for (Long id : descendantIds) {
            categoryMapper.updateProductCategoryId(id, parentId);
        }

        // 删除分类及其子分类
        for (Long id : descendantIds) {
            categoryMapper.delete(id);
        }
    }

    /**
     * 修改分类名称
     * @param categoryId 分类ID
     * @param newName 新名称
     */
    @Override
    @Transactional
    public void updateCategoryName(Long categoryId, String newName) {
        // 验证分类是否存在
        Category category = categoryMapper.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }

        // 更新分类名称
        categoryMapper.updateName(categoryId, newName);
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