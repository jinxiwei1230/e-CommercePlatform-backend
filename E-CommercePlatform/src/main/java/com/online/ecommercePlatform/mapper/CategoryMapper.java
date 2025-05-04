package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 获取所有分类
    @Select("SELECT category_id, name, parent_id FROM category")
    List<Category> alllist();

    // 根据父ID获取子分类
    @Select("SELECT category_id, name, parent_id FROM category WHERE parent_id = #{parentId}")
    List<Category> findByParentId(Long parentId);

    // 插入新分类
    @Insert("INSERT INTO category (name, parent_id, create_time) VALUES (#{name}, #{parentId}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    void insert(Category category);

    // 删除分类
    @Delete("DELETE FROM category WHERE category_id = #{categoryId}")
    void delete(Long categoryId);

    // 更新分类名称
    @Update("UPDATE category SET name = #{name} WHERE category_id = #{categoryId}")
    void updateName(@Param("categoryId") Long categoryId, @Param("name") String name);

    // 获取分类下的所有子分类ID（包括自身）
    @Select("WITH RECURSIVE category_tree AS (" +
            "  SELECT category_id FROM category WHERE category_id = #{categoryId} " +
            "  UNION ALL " +
            "  SELECT c.category_id FROM category c " +
            "  INNER JOIN category_tree ct ON c.parent_id = ct.category_id" +
            ") SELECT category_id FROM category_tree")
    List<Long> findAllDescendantIds(Long categoryId);

    // 将商品的分类ID更新为父分类ID
    @Update("UPDATE product SET category_id = #{parentId} WHERE category_id = #{categoryId}")
    void updateProductCategoryId(@Param("categoryId") Long categoryId, @Param("parentId") Long parentId);

    // 根据分类ID获取分类
    @Select("SELECT category_id, name, parent_id, create_time FROM category WHERE category_id = #{categoryId}")
    Category findById(Long categoryId);
}
