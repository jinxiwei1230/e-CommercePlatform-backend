package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CategoryHotProductsDTO;
import com.online.ecommercePlatform.dto.ProductBasicInfoDTO;
import com.online.ecommercePlatform.dto.ProductDTO;
import com.online.ecommercePlatform.dto.ProductDetailDTO;
import com.online.ecommercePlatform.dto.ProductQueryDTO;
import com.online.ecommercePlatform.dto.ImageUploadDTO;
import com.online.ecommercePlatform.dto.ProductBriefDTO;
import com.online.ecommercePlatform.dto.ProductCreateDTO;
import com.online.ecommercePlatform.dto.ProductUpdateDTO;
import com.online.ecommercePlatform.dto.ImageMainStatusUpdateDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.dto.PageResult;
import com.online.ecommercePlatform.service.ProductService;
import com.online.ecommercePlatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品控制器，处理产品相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService; // 产品服务，用于处理产品业务逻辑

    /**
     * 获取首页轮播展示的热门商品
     * @param limit 查询数量(可选，默认5)
     * @return Result包装的热门商品列表
     */
    @GetMapping("/hot")
    public Result<List<ProductBasicInfoDTO>> getHotProducts(
                    @RequestParam(required = false,
                    defaultValue = "5") int limit) {
        return productService.getHotProducts(limit);
    }

    /**
     * 获取热门类别及其热门商品API
     * @return 统一响应结果，包含4个热门类别及其各自5个热门商品
     */
    @GetMapping("/categories/hot")
    public Result<List<CategoryHotProductsDTO>> getHotCategoriesAndProducts() {
        return productService.getHotCategoriesAndProducts();
    }

    /**
     * 根据类别ID获取商品列表（支持分页、排序和价格筛选）
     *
     * @param categoryId 商品类别ID（路径变量）
     * @param page 当前页码，默认为1
     * @param pageSize 每页显示数量，默认为10
     * @param sortBy 排序字段（可选）
     * @param sortOrder 排序方式（asc/desc，可选）
     * @param minPrice 最低价格筛选（可选）
     * @param maxPrice 最高价格筛选（可选）
     * @return 包含分页商品列表的Result对象
     */
    @GetMapping("/{categoryId}/products")
    public Result<PageBean<ProductDTO>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        try {
            // 参数校验
            if (categoryId == null || categoryId.trim().isEmpty()) {
                return Result.error(Result.BAD_REQUEST, "类别 ID 不能为空");
            }
            Long categoryIdLong;
            try {
                // 特殊处理 "all" 或其他非数字 categoryId 的情况，如果 ProductQueryDTO 的 categoryId 为 null 则代表所有
                if ("all".equalsIgnoreCase(categoryId)) { // 假设 "all" 是一个有效值代表所有分类
                    categoryIdLong = null; // 或者根据业务逻辑处理
                } else {
                    categoryIdLong = Long.valueOf(categoryId);
                }
            } catch (NumberFormatException e) {
                return Result.error(Result.BAD_REQUEST, "类别 ID 格式无效");
            }

            if (page < 1 || pageSize < 1) {
                return Result.error(Result.BAD_REQUEST, "分页参数无效");
            }
            if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                return Result.error(Result.BAD_REQUEST, "最低价格不能大于最高价格");
            }
            ProductQueryDTO queryDTO = new ProductQueryDTO();
            queryDTO.setCategoryId(categoryIdLong); // 使用转换后的 Long 类型
            queryDTO.setPage(page);
            queryDTO.setPageSize(pageSize);
            queryDTO.setSortBy(sortBy);
            queryDTO.setSortOrder(sortOrder);
            queryDTO.setMinPrice(minPrice);
            queryDTO.setMaxPrice(maxPrice);

            List<ProductDTO> products = productService.getProductsByCategory(queryDTO);
            Long total = (long) productService.countProductsByCategory(queryDTO); // 注意类型转换

            PageBean<ProductDTO> pageBean = new PageBean<>();
            pageBean.setTotal(total);
            pageBean.setItems(products);
            return Result.success(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "服务器错误: " + e.getMessage());
        }
    }
    
    /**
     * 获取商品详情
     * @param id 商品ID
     * @return 包含商品详情的Result对象
     */
    @GetMapping("/{id}")
    public Result<ProductDetailDTO> getProductDetail(@PathVariable("id") String id) {
        try {
            // 参数校验
            if (id == null || id.trim().isEmpty()) {
                return Result.error(Result.BAD_REQUEST, "商品 ID 不能为空");
            }
            
            Long productId;
            try {
                productId = Long.valueOf(id);
            } catch (NumberFormatException e) {
                return Result.error(Result.BAD_REQUEST, "商品 ID 格式无效");
            }
            
            // 调用服务获取商品详情
            return productService.getProductDetail(productId);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "获取商品详情失败: " + e.getMessage());
        }
    }

    /**
     * 上传商品图片 (Base64 方式)
     * @param imageUploadDTO 包含 productId 和 imageDataWithPrefix
     * @return 操作结果
     */
    @PostMapping("/images/upload") // 定义上传接口路径
    public Result<?> uploadProductImageBase64(@RequestBody ImageUploadDTO imageUploadDTO) {
         try {
            // 调用 Service 层处理上传逻辑
            return productService.uploadProductImage(imageUploadDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品列表 (支持分页、按名称搜索、价格区间筛选、分类筛选和排序)
     *
     * @param queryDTO 包含所有查询参数的对象
     * @return 包含分页商品列表的 Result 对象 (使用 PageResult 和 ProductBriefDTO)
     */
    @GetMapping
    public Result<PageResult<ProductBriefDTO>> getProducts(ProductQueryDTO queryDTO) {
        // 参数校验可以放在 Service 层，或者在这里进行初步校验
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        // 对于 minPrice 和 maxPrice 的校验，如果需要也可以在这里添加
        // 例如: if (queryDTO.getMinPrice() != null && queryDTO.getMaxPrice() != null && queryDTO.getMinPrice().compareTo(queryDTO.getMaxPrice()) > 0) {
        // return Result.error(Result.BAD_REQUEST, "最低价格不能大于最高价格");
        // }

        PageResult<ProductBriefDTO> productPageResult = productService.listProducts(queryDTO);
        return Result.success(productPageResult);
    }

    /**
     * 添加新商品
     * @param productCreateDTO 商品创建信息
     * @return 包含新创建商品信息的 Result 对象 (主要是 ID)
     */
    @PostMapping
    public ResponseEntity<Result<ProductBriefDTO>> addProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        // TODO: 在 Service 层或此处进行更详细的参数校验，例如非空、格式等
        // 例如：if (productCreateDTO.getName() == null || productCreateDTO.getName().trim().isEmpty()) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(Result.BAD_REQUEST, "商品名称不能为空"));
        // }

        ProductBriefDTO newProductBrief = productService.createProduct(productCreateDTO); // 假设 service 方法叫 createProduct
        
        // 如果你的 Result.success() 方法总是返回 200 的 code，
        // 并且你想严格遵循 REST 返回 201 Created，可以这样包装：
        // Result<ProductBriefDTO> successResult = Result.success(newProductBrief);
        // successResult.setCode(HttpStatus.CREATED.value()); // 或者你的Result类有专门的 created 方法
        // return new ResponseEntity<>(successResult, HttpStatus.CREATED);
        
        // 或者，如果你的 Result.java 中 code 字段更多的是业务层面成功（如0或1），
        // 而 HTTP 状态码由 ResponseEntity 控制，可以这样：
        return new ResponseEntity<>(Result.success(newProductBrief), HttpStatus.CREATED);
    }

    /**
     * 更新指定ID商品的信息
     * @param id 商品ID
     * @param updateDTO 包含要更新的商品信息的DTO
     * @return 更新后的商品信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<ProductBriefDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO updateDTO) {
        try {
            ProductBriefDTO updatedProduct = productService.updateProductInfo(id, updateDTO);
            return ResponseEntity.ok(Result.success(updatedProduct));
        } catch (ResourceNotFoundException e) {
            // 你需要定义一个全局异常处理器来处理 ResourceNotFoundException 并返回 404
            // 或者在这里直接返回。为了演示，我直接返回。
            // Result.NOT_FOUND 是 Result 类中定义的错误码 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.error(Result.NOT_FOUND, e.getMessage())); 
        } catch (IllegalArgumentException e) { // 用于处理其他校验类错误，如果Service层抛出
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(Result.BAD_REQUEST, e.getMessage()));
        }
        // 其他潜在的异常也应该被捕获和处理，或者由全局异常处理器处理
    }

    /**
     * 删除指定ID的商品图片
     * @param imageId 要删除的图片ID
     * @return 操作结果
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Result<Object>> deleteProductImage(@PathVariable Long imageId) {
        try {
            productService.deleteProductImage(imageId);
            // 返回 200 OK 带消息体
            return ResponseEntity.ok(Result.success("图片删除成功"));
            // 或者返回 204 No Content (更符合REST语义，但前端可能需要不同处理)
            // return ResponseEntity.noContent().build(); 
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Result.error(Result.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            // 其他通用服务器错误
            // 最好有一个全局异常处理器来处理这类未预期的错误
            e.printStackTrace(); // 记录日志
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Result.error(Result.SERVER_ERROR, "图片删除失败: " + e.getMessage()));
        }
    }

    /**
     * 修改指定商品图片的"主图"状态。
     * @param imageId 要操作的图片ID
     * @param statusUpdateDTO 包含 isMain 状态的请求体
     * @return 操作结果
     */
    @PutMapping("/images/{imageId}/main")
    public ResponseEntity<Result<Object>> updateImageMainStatus(
            @PathVariable Long imageId,
            @Valid @RequestBody ImageMainStatusUpdateDTO statusUpdateDTO) { // 使用 @Valid 触发校验
        
        // @NotNull 在DTO中已经定义，如果 statusUpdateDTO.getIsMain() 为 null，
        // Spring 的 MethodArgumentNotValidException 会被触发，
        // 通常由全局异常处理器 @ControllerAdvice 捕获并返回400。
        // 如果没有全局处理器，这里需要 try-catch MethodArgumentNotValidException 或手动校验。
        
        // 手动校验示例 (如果不用@Valid或没有全局异常处理器处理MethodArgumentNotValidException):
        // if (statusUpdateDTO.getIsMain() == null) {
        //     return ResponseEntity.badRequest()
        //                         .body(Result.error(Result.BAD_REQUEST, "isMain 字段为必需且必须是布尔值"));
        // }

        try {
            productService.updateProductImageMainStatus(imageId, statusUpdateDTO.getIsMain());
            return ResponseEntity.ok(Result.success("主图状态更新成功"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Result.error(Result.NOT_FOUND, e.getMessage()));
        } catch (IllegalArgumentException e) { // 可以用于Service层其他业务校验，如尝试取消不存在的主图等
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Result.error(Result.BAD_REQUEST, e.getMessage()));
        }
        // No need for catch (Exception e) here if you have a global exception handler for 500s
        // otherwise, keep it for unhandled server errors.
    }
}
