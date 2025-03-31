package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 获取所有分类
    @Select("SELECT category_id, name, parent_id FROM category")
    List<Category> alllist();

    // 根据父ID获取子分类
    @Select("SELECT category_id, name, parent_id FROM category WHERE parent_id = #{parentId}")
    List<Category> findByParentId(Long parentId);
}
