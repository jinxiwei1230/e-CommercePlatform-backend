package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 商品数据访问层接口，定义对商品表的基本操作
 */
@Mapper
public interface ProductMapper {

    /**
     * 通过商品 ID 查找商品
     * @param id 商品 ID
     * @return 商品对象
     */
    Product findById(Long id);

    /**
     * 根据关键词、类别、价格范围搜索商品
     * @param keyword 搜索关键词
     * @param categoryId 商品类别 ID（可选）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 商品列表
     */
    List<Product> search(String keyword, Long categoryId, Double minPrice, Double maxPrice);

    /**
     * 插入新的商品
     * @param product 商品对象
     */
    void insert(Product product);

    /**
     * 更新商品信息
     * @param product 商品对象
     */
    void update(Product product);

    /**
     * 根据商品 ID 删除商品
     * @param id 商品 ID
     */
    void delete(Long id);
}
