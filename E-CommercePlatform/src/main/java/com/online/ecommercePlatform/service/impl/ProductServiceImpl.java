package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.ProductImage;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.ProductService;
import com.online.ecommercePlatform.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品服务实现类，处理产品的业务逻辑
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper; // 注入产品数据访问层接口，用于数据库操作

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
     * 获取商品详情，包括基本信息、分类名和图片列表
     * @param productId 商品ID
     * @return 包含商品详情的Result对象
     */
    @Override
    public Result<ProductDetailDTO> getProductDetail(Long productId) {
        try {
            // 获取商品基本信息
            Map<String, Object> productMap = productMapper.getProductDetail(productId);
            if (productMap == null || productMap.isEmpty()) {
                return Result.error(Result.NOT_FOUND, "商品不存在");
            }
            
            // 获取商品图片
            List<Map<String, Object>> productImages = productMapper.getProductImages(productId);
            
            // 构建ProductDetailDTO
            ProductDetailDTO detailDTO = new ProductDetailDTO();
            
            // 从Map中获取商品信息
            detailDTO.setProductId(productMap.get("productId").toString());
            detailDTO.setName((String) productMap.get("name"));
            detailDTO.setDescription((String) productMap.get("description"));
            detailDTO.setPrice(((Number) productMap.get("price")).doubleValue());
            detailDTO.setStock(((Number) productMap.get("stock")).intValue());
            detailDTO.setSales(((Number) productMap.get("sales")).intValue());
            detailDTO.setCategoryId(productMap.get("categoryId").toString());
            
            // 从Map中获取分类名称
            Object categoryName = productMap.get("categoryName");
            if (categoryName != null) {
                detailDTO.setCategoryName(categoryName.toString());
            }
            
            // 转换图片列表 (这里的 imageUrl 已经是 Base64 Data URL)
            if (productImages != null && !productImages.isEmpty()) {
                List<ProductDetailDTO.ProductImageDTO> imageDTOs = productImages.stream()
                        .map(image -> {
                            ProductDetailDTO.ProductImageDTO imageDTO = new ProductDetailDTO.ProductImageDTO();
                            imageDTO.setImageId(String.valueOf(image.get("id")));
                            // 直接使用从数据库获取的 imageUrl (Base64 Data URL)
                            imageDTO.setImageUrl((String) image.get("image_url")); 
                            Integer sortOrder = image.get("sort_order") instanceof Number ? ((Number)image.get("sort_order")).intValue() : 0;
                            imageDTO.setSortOrder(sortOrder);
                            // 如果需要 isMain 字段，也从 map 中获取
                            // Boolean isMain = (Boolean) image.get("is_main");
                            // imageDTO.setIsMain(isMain != null ? isMain : false);
                            return imageDTO;
                        })
                        .collect(Collectors.toList());
                detailDTO.setImages(imageDTOs);
            } else {
                detailDTO.setImages(new ArrayList<>());
            }
            
            return Result.success(detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "获取商品详情失败: " + e.getMessage());
        }
    }

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

    /**
     * 获取热门产品
     * @param limit 返回数量
     * @return 包含产品列表的Result对象
     */
    @Override
    public Result<List<ProductBasicInfoDTO>> getHotProducts(int limit) {
        List<ProductBasicInfoDTO> products = productMapper.selectHotProducts(limit);
        return Result.success(products);
    }


    /**
     * 获取顶级热门类别及其热门商品列表实现
     * @return 统一响应结果，包含4个顶级热门类别及其各自5个热门商品列表
     */
    @Override
    public Result<List<CategoryHotProductsDTO>> getHotCategoriesAndProducts() {
        // 固定参数：4个顶级类别，每个类别5个商品
        final int TOP_CATEGORIES_LIMIT = 4;
        final int PRODUCTS_PER_CATEGORY = 5;

        // 获取所有顶级类别及其子类别销量总和，并取前4
        List<CategoryHotProductsDTO> hotCategories = productMapper.selectTopLevelCategoriesWithSubSales(TOP_CATEGORIES_LIMIT);

        if (hotCategories == null || hotCategories.isEmpty()) {
            return Result.error(Result.BAD_REQUEST, "暂无顶级热门类别数据");
        }

        // 为每个顶级类别获取前5个热门商品（包含子类别中的商品）
        for (CategoryHotProductsDTO category : hotCategories) {
            List<ProductBasicInfoDTO> products = productMapper.selectHotProductsByTopCategory(
                    category.getCategoryId(),
                    PRODUCTS_PER_CATEGORY
            );
            category.setHotProducts(products.isEmpty() ? new ArrayList<>() : products);
        }

        return Result.success(hotCategories);
    }

    /**
     * 根据查询条件获取商品列表实现
     * 支持查询特定分类或所有分类的商品，并支持分页、排序和价格筛选
     * @param queryDTO 商品查询DTO
     * @return 商品DTO列表
     */
    @Override
    public List<ProductDTO> getProductsByCategory(ProductQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        if (queryDTO.isAllProducts()) {
            // 查询所有商品
            return productMapper.findAllProducts(
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice(),
                    queryDTO.getSortBy(),
                    queryDTO.getSortOrder()
            );
        } else {
            // 查询特定分类商品
            return productMapper.findByCategory(
                    Long.valueOf(queryDTO.getCategoryId()),
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice(),
                    queryDTO.getSortBy(),
                    queryDTO.getSortOrder()
            );
        }
    }

    /**
     * 根据查询条件统计商品数量实现
     * 支持统计特定分类或所有分类的商品数量，并支持价格筛选
     * @param queryDTO 商品查询DTO
     * @return 商品总数
     */
    @Override
    public int countProductsByCategory(ProductQueryDTO queryDTO) {
        if (queryDTO.isAllProducts()) {
            // 统计所有商品
            return productMapper.countAllProducts(
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice()
            );
        } else {
            // 统计特定分类商品
            return productMapper.countByCategory(
                    Long.valueOf(queryDTO.getCategoryId()),
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice()
            );
        }
    }

    @Override
    @Transactional // 添加事务注解，确保操作原子性
    public Result<?> uploadProductImage(ImageUploadDTO imageUploadDTO) {
        // 验证 productId 和 imageData
        if (imageUploadDTO == null || imageUploadDTO.getProductId() == null || imageUploadDTO.getImageDataWithPrefix() == null || imageUploadDTO.getImageDataWithPrefix().isEmpty()) {
            return Result.error(Result.BAD_REQUEST, "商品ID和图片数据不能为空");
        }
        // 简单验证 Data URL 格式 (可选但推荐)
        if (!imageUploadDTO.getImageDataWithPrefix().startsWith("data:image")) {
             return Result.error(Result.BAD_REQUEST, "无效的图片数据格式");
        }

        try {
            // 检查商品是否存在 (可选)
            // Product product = productMapper.findById(imageUploadDTO.getProductId());
            // if (product == null) {
            //     return Result.error(Result.NOT_FOUND, "关联的商品不存在");
            // }
            
            // 1. 创建 ProductImage 实体
            ProductImage productImage = new ProductImage();
            productImage.setProductId(imageUploadDTO.getProductId());
            productImage.setImageUrl(imageUploadDTO.getImageDataWithPrefix()); // 直接存储完整的 Data URL
            // 从 DTO 获取 sortOrder 和 isMain (如果需要)
            // productImage.setSortOrder(imageUploadDTO.getSortOrder() != null ? imageUploadDTO.getSortOrder() : 0);
            // productImage.setMain(imageUploadDTO.getIsMain() != null ? imageUploadDTO.getIsMain() : false);
            productImage.setCreatedTime(LocalDateTime.now()); // 设置创建时间

            // 2. 保存到数据库
            productMapper.insertProductImage(productImage);

            // 可以返回成功信息或者新图片的ID
            return Result.success("图片上传成功"); 
            // return Result.success(productImage.getId()); // 如果需要返回ID，确保insertProductImage后能获取到ID

        } catch (Exception e) { // 捕获更广泛的异常，例如数据库错误
            e.printStackTrace(); // 记录错误日志
            // Consider rolling back the transaction if using @Transactional
            return Result.error(Result.SERVER_ERROR, "保存图片时发生错误: " + e.getMessage());
        }
    }
}
