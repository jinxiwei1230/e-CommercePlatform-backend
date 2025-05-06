package com.online.ecommercePlatform.mapper;
import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.dto.ProductDTO;
import com.online.ecommercePlatform.pojo.ProductImage;
import org.apache.ibatis.annotations.Param;
import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.online.ecommercePlatform.dto.ProductQueryDTO;

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
     * 根据分类ID查询商品列表
     * @param categoryId 分类ID
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @param sortBy 排序字段（可选）
     * @param sortOrder 排序方式（可选）
     * @return 商品DTO列表
     */
    List<ProductDTO> findByCategory(@Param("categoryId") Long categoryId,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder);

    /**
     * 根据分类ID统计商品数量
     * @param categoryId 分类ID
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 商品数量
     */
    Integer countByCategory(@Param("categoryId") Long categoryId,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * 查询所有商品列表（不限定分类）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @param sortBy 排序字段（可选）
     * @param sortOrder 排序方式（可选）
     * @return 商品DTO列表
     */
    List<ProductDTO> findAllProducts(@Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("sortBy") String sortBy,
                                  @Param("sortOrder") String sortOrder);

    /**
     * 统计所有商品数量（不限定分类）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 商品数量
     */
    Integer countAllProducts(@Param("minPrice") BigDecimal minPrice,
                             @Param("maxPrice") BigDecimal maxPrice);


    /**
     * 通过商品 ID 查找商品
     * @param id 商品 ID
     * @return 商品对象
     */
    Product findById(Long id);
    
    /**
     * 根据商品ID获取商品详情，包括基本信息、分类名称等
     * @param productId 商品ID
     * @return 包含商品信息的Map
     */
    Map<String, Object> getProductDetail(@Param("productId") Long productId);
    
    /**
     * 根据商品ID获取商品的所有图片
     * @param productId 商品ID
     * @return 图片列表
     */
    List<Map<String, Object>> getProductImages(@Param("productId") Long productId);

    /**
     * 插入新的商品图片
     * @param productImage 商品图片对象 (包含 Base64 Data URL)
     */
    void insertProductImage(ProductImage productImage);

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

    /**
     * 根据查询条件动态查询商品列表 (带分页、筛选、排序)
     * 返回的 Map 应包含 product.*, category.name as categoryName, 和主图 URL as mainImageUrl
     * @param queryDTO 查询参数 DTO
     * @return 商品信息 Map 列表
     */
    List<Map<String, Object>> selectProductList(ProductQueryDTO queryDTO);

    /**
     * 根据图片ID查找商品图片信息
     * @param imageId 图片ID
     * @return 商品图片对象，如果不存在则返回 null
     */
    ProductImage findImageById(Long imageId);

    /**
     * 根据图片ID删除商品图片
     * @param imageId 图片ID
     * @return 删除的行数
     */
    int deleteImageById(Long imageId);
}
