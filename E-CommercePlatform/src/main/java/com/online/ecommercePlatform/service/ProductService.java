package com.online.ecommercePlatform.service;
import com.online.ecommercePlatform.mapper.ProductMapper;
import com.online.ecommercePlatform.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 产品服务接口，定义产品的业务逻辑方法
 */
public interface ProductService {

    /**
     * 搜索产品，根据关键字、类别 ID、最低价格和最高价格进行筛选
     * @param keyword 关键字
     * @param categoryId 类别 ID（可选）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 符合条件的产品列表
     */
    List<Product> searchProducts(String keyword, Long categoryId, Double minPrice, Double maxPrice);

    /**
     * 根据产品 ID 获取产品信息
     * @param id 产品 ID
     * @return 对应的产品对象
     */
    Product getProductById(Long id);



    /**
     * 产品服务接口，定义管理员可以执行的产品管理操作
     */
    /**
     * 添加新产品
     * @param product 产品对象
     * @return 添加后的产品对象
     */
    Product addProduct(Product product);

    /**
     * 更新产品信息
     * @param product 更新后的产品对象
     * @return 更新后的产品对象
     */
    Product updateProduct(Product product);

    /**
     * 删除产品
     * @param id 产品 ID
     */
    void deleteProduct(Long id);
}
