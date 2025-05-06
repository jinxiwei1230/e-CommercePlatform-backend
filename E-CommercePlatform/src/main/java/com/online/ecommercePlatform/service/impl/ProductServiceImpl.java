package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.online.ecommercePlatform.dto.*;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.ProductImage;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.ProductService;
import com.online.ecommercePlatform.mapper.ProductMapper;
import com.online.ecommercePlatform.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

/**
 * 产品服务实现类，处理产品的业务逻辑
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper; // 注入产品数据访问层接口，用于数据库操作

    @Autowired(required = false) // CategoryMapper 可能不是必须的，如果 productMapper 能直接返回分类名
    private CategoryMapper categoryMapper;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                            
                            // 获取并设置 isMain 字段
                            Object isMainObj = image.get("is_main"); // 从 Map 中获取 is_main 列的值
                            Boolean isMain = false; // 默认值
                            if (isMainObj instanceof Boolean) {
                                isMain = (Boolean) isMainObj;
                            } else if (isMainObj instanceof Number) {
                                // 处理数据库返回 0 或 1 的情况 (例如 TINYINT(1))
                                isMain = ((Number) isMainObj).intValue() != 0;
                            }
                            imageDTO.setIsMain(isMain); // 设置到 DTO
                            
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
        if (queryDTO.getCategoryId() == null) {
            // 查询所有商品
            return productMapper.findAllProducts(
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice(),
                    queryDTO.getSortByOrDefault(),
                    queryDTO.getSortOrder()
            );
        } else {
            // 查询特定分类商品
            return productMapper.findByCategory(
                    queryDTO.getCategoryId(),
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice(),
                    queryDTO.getSortByOrDefault(),
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
        if (queryDTO.getCategoryId() == null) {
            // 统计所有商品
            return productMapper.countAllProducts(
                    queryDTO.getMinPrice(),
                    queryDTO.getMaxPrice()
            );
        } else {
            // 统计特定分类商品
            return productMapper.countByCategory(
                    queryDTO.getCategoryId(),
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
            
            // 处理 sortOrder 和 isMain (如果 DTO 中提供了值，则使用；否则使用默认值)
            productImage.setSortOrder(imageUploadDTO.getSortOrder() != null ? imageUploadDTO.getSortOrder() : 0); // 默认排序 0
            productImage.setIsMain(imageUploadDTO.getIsMain() != null ? imageUploadDTO.getIsMain() : false); // 默认非主图
            
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

    @Override
    public PageResult<ProductBriefDTO> listProducts(ProductQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        // 调用 Mapper 获取包含商品基本信息、分类名和主图URL的数据列表
        // 假设 ProductMapper 有一个 selectProductList 方法
        List<Map<String, Object>> productMaps = productMapper.selectProductList(queryDTO);
        
        long total = ((Page<Map<String, Object>>) productMaps).getTotal();

        List<ProductBriefDTO> briefDTOs = productMaps.stream().map(productMap -> {
            ProductBriefDTO dto = new ProductBriefDTO();
            dto.setProductId(((Number) productMap.get("productId")).longValue());
            dto.setName((String) productMap.get("name"));
            dto.setMainImageUrl((String) productMap.get("mainImageUrl")); // 假设 mapper 返回了 mainImageUrl
            Object priceObj = productMap.get("price");
            if (priceObj instanceof Number) {
                 dto.setPrice(((Number) priceObj).doubleValue());
            }
            Object stockObj = productMap.get("stock");
            if (stockObj instanceof Number) {
                dto.setStock(((Number) stockObj).intValue());
            }
            Object salesObj = productMap.get("sales");
            if (salesObj instanceof Number) {
                dto.setSales(((Number) salesObj).intValue());
            }
            Object shippingFeeObj = productMap.get("shippingFee"); // 或 "freight"
            if (shippingFeeObj instanceof Number) {
                dto.setShippingFee(((Number) shippingFeeObj).doubleValue());
            }
            LocalDateTime createTime = (LocalDateTime) productMap.get("createTime");
            if (createTime != null) {
                dto.setCreatedAt(createTime.format(DATETIME_FORMATTER));
            }
            dto.setCategoryName((String) productMap.get("categoryName")); // 假设 mapper 返回了 categoryName
            return dto;
        }).collect(Collectors.toList());

        return new PageResult<>(total, queryDTO.getPage(), queryDTO.getPageSize(), briefDTOs);
    }

    @Override
    @Transactional // 确保操作的原子性
    public ProductBriefDTO createProduct(ProductCreateDTO createDTO) {
        Product product = new Product();
        product.setName(createDTO.getName());
        product.setCategoryId(createDTO.getCategoryId());
        
        // BigDecimal to Double conversion for price and freight if Product entity uses Double
        // If Product entity uses BigDecimal for price/freight, direct set is fine.
        // Assuming Product.price and Product.freight are Double as per Product.java earlier.
        if (createDTO.getPrice() != null) {
            product.setPrice(createDTO.getPrice().doubleValue()); 
        }
        if (createDTO.getShippingFee() != null) {
            product.setFreight(createDTO.getShippingFee().doubleValue()); // DTO is shippingFee, Entity is freight
        }
        product.setStock(createDTO.getStock());
        product.setDescription(createDTO.getDescription());

        // createTime 和 updateTime 由数据库 DEFAULT CURRENT_TIMESTAMP 自动设置，
        // 或者如果需要在Java端设置，可以在这里 product.setCreateTime(LocalDateTime.now());
        // product.setSales(0); // 销量默认为0，数据库也有 DEFAULT 0

        productMapper.insert(product); // 调用 mapper 插入，并获取生成的主键

        // product 对象现在应该包含由数据库生成的 productId
        // 如果 productMapper.insert 没有配置返回主键, product.getProductId() 会是 null
        // 确保 ProductMapper.xml 中的 <insert> 配置了 useGeneratedKeys 和 keyProperty

        // 转换回 ProductBriefDTO 返回
        ProductBriefDTO briefDTO = new ProductBriefDTO();
        briefDTO.setProductId(product.getProductId());
        briefDTO.setName(product.getName());
        briefDTO.setPrice(product.getPrice()); // price
        briefDTO.setStock(product.getStock());
        briefDTO.setShippingFee(product.getFreight()); // freight
        // briefDTO.setSales(product.getSales()); // 新商品销量是0，可不返回或返回0
        // briefDTO.setMainImageUrl(null); // 新商品还没有图片
        // briefDTO.setCategoryName(null); // 需要查询分类名，如果响应需要的话
        // if (product.getCreateTime() != null) {
        //    briefDTO.setCreatedAt(product.getCreateTime().format(DATETIME_FORMATTER));
        // }
        
        // 根据响应示例，只需要 productId 和 name 等核心信息。 
        // 如果需要返回更多信息如 categoryName，则需要额外查询。
        // 你的成功响应示例只列出了 productId 和 name
        // "data": { "productId": "124", "name": "新商品名称", ... }
        // 为了匹配这个，我们可以只填充这两个，或者像ProductBriefDTO那样完整填充（除了图片和分类名）

        return briefDTO; // 返回包含新ID和其他信息的DTO
    }
}
