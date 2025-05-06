package com.online.ecommercePlatform.service;
import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.mapper.ProductMapper;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 产品服务接口，定义产品的业务逻辑方法
 */
public interface ProductService {

    /**
     * 根据产品 ID 获取产品信息
     * @param id 产品 ID
     * @return 对应的产品对象
     */
    Product getProductById(Long id);
    
    /**
     * 获取商品详情，包括基本信息、分类名和图片列表
     * @param productId 商品ID
     * @return 包含商品详情的Result对象
     */
    Result<ProductDetailDTO> getProductDetail(Long productId);

    /**
     * 上传并保存商品图片
     * @param imageUploadDTO 包含 productId 和 Base64 Data URL 的 DTO
     * @return 操作结果
     */
    Result<?> uploadProductImage(ImageUploadDTO imageUploadDTO);

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

    /**
     * 获取热门产品
     * @param limit 返回数量
     */
    Result<List<ProductBasicInfoDTO>> getHotProducts(int limit);

    /**
     * 获取热门类别及其热门商品列表实现
     * @return 统一响应结果，包含热门类别及其商品列表
     */
    Result<List<CategoryHotProductsDTO>> getHotCategoriesAndProducts();

    /**
     * 根据查询条件获取商品列表
     * @param queryDTO 商品查询DTO，包含分类、分页、排序和价格筛选条件
     * @return 商品DTO列表
     */
    List<ProductDTO> getProductsByCategory(ProductQueryDTO queryDTO);

    /**
     * 根据查询条件统计商品数量
     * @param queryDTO 商品查询DTO，包含分类和价格筛选条件
     * @return 商品总数
     */
    int countProductsByCategory(ProductQueryDTO queryDTO);

    /**
     * 获取商品列表 (带筛选、分页、排序)
     * @param queryDTO 包含所有查询参数的 DTO
     * @return 分页后的商品简要信息列表
     */
    PageResult<ProductBriefDTO> listProducts(ProductQueryDTO queryDTO);

    /**
     * 创建新商品
     * @param createDTO 包含新商品信息的 DTO
     * @return 包含新创建商品（主要是ID和名称等）的简要信息 DTO
     */
    ProductBriefDTO createProduct(ProductCreateDTO createDTO);

    /**
     * 更新指定ID商品的信息。
     * @param productId 要更新的商品ID
     * @param updateDTO 包含要更新的字段的DTO
     * @return 更新后的商品简要信息DTO
     * @throws ResourceNotFoundException 如果商品未找到
     */
    ProductBriefDTO updateProductInfo(Long productId, ProductUpdateDTO updateDTO);
}
