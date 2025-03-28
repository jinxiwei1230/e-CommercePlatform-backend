package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.service.ProductService;
import com.online.ecommercePlatform.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品服务实现类，处理产品的业务逻辑
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper; // 注入产品数据访问层接口，用于数据库操作

    /**
     * 搜索产品，根据关键字、类别 ID、最低价格和最高价格进行筛选
     * @param keyword 关键字
     * @param categoryId 类别 ID（可选）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 符合条件的产品列表
     */
    @Override
    public List<Product> searchProducts(String keyword, Long categoryId, Double minPrice, Double maxPrice) {
        return productMapper.search(keyword, categoryId, minPrice, maxPrice);
    }

    /**
     * 根据产品 ID 获取产品信息
     * @param id 产品 ID
     * @return 对应的产品对象
     */
    @Override
    public Product getProductById(Long id) {
        return productMapper.findById(id);
    }



    /**
     * 产品服务实现类，处理管理员对产品的增、删、改操作
     */
    /**
     * 添加新产品
     * @param product 产品对象
     * @return 添加后的产品对象
     */
    @Override
    public Product addProduct(Product product) {
        productMapper.insert(product); // 插入产品数据
        return product;
    }

    /**
     * 更新产品信息
     * @param product 更新后的产品对象
     * @return 更新后的产品对象
     */
    @Override
    public Product updateProduct(Product product) {
        productMapper.update(product); // 更新产品数据
        return product;
    }

    /**
     * 删除产品
     * @param id 产品 ID
     */
    @Override
    public void deleteProduct(Long id) {
        productMapper.delete(id);
    }
}
