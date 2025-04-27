package com.online.ecommercePlatform.mapper;
import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.dto.ProductDTO;
import org.apache.ibatis.annotations.Param;
import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品数据访问层接口，定义对商品表的基本操作
 */
@Mapper
public interface ProductMapper {

    /**
     * 查询热门商品基础信息
     * @param limit 查询条数
     * @return 商品基础信息列表
     */
    List<ProductBasicInfoDTO> selectHotProducts(@Param("limit") int limit);

    /**
     * 查询指定分类的热门商品
     * @param categoryId 分类ID
     * @param limit 查询数量
     * @return 商品基础信息DTO列表
     */
    List<ProductBasicInfoDTO> selectHotProductsByTopCategory(
            @Param("categoryId") Long categoryId,
            @Param("limit") int limit);

    /**
     * 获取热门类别
     * @param i 获取销量最高的i个类别
     * @return 统一响应结果，包含热门类别及其商品列表
     */
    List<CategoryHotProductsDTO> selectTopLevelCategoriesWithSubSales(int i);

    /**
     * 通过商品 ID 查找商品
     * @param id 商品 ID
     * @return 商品对象
     */
    @Select("")
    Product findById(Long id);

    /**
     * 插入新的商品
     * @param product 商品对象
     */
    @Select("")
    void insert(Product product);

    /**
     * 更新商品信息
     * @param product 商品对象
     */
    @Select("")
    void update(Product product);

    /**
     * 根据商品 ID 删除商品
     * @param id 商品 ID
     */
    @Select("")
    void delete(Long id);

    List<ProductDTO> findByCategory(@Param("categoryId") Long categoryId,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder);

    Integer countByCategory(@Param("categoryId") Long categoryId,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice);

    List<ProductDTO> findAllProducts(@Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("sortBy") String sortBy,
                                  @Param("sortOrder") String sortOrder);

    Integer countAllProducts(@Param("minPrice") BigDecimal minPrice,
                             @Param("maxPrice") BigDecimal maxPrice);

}
