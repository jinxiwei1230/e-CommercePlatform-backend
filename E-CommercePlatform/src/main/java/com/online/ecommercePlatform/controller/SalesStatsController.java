package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.CategorySalesStatsDTO;
import com.online.ecommercePlatform.dto.ProductSalesStatsDTO;
import com.online.ecommercePlatform.dto.SalesStatsQueryDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.SalesStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 销量统计控制器
 */
@RestController
@RequestMapping("/api/stats/sales")
public class SalesStatsController {

    @Autowired
    private SalesStatsService salesStatsService;

    /**
     * 获取商品销量统计
     * @param categoryId 分类ID（可选，不传查询所有分类）
     * @param startDate 开始日期（可选，格式：yyyy-MM-dd）
     * @param endDate 结束日期（可选，格式：yyyy-MM-dd）
     * @param sortBy 排序字段（可选，sales:按销量排序，amount:按销售额排序）
     * @param sortOrder 排序方式（可选，asc:升序，desc:降序）
     * @param pageNum 页码（可选，默认1）
     * @param pageSize 每页条数（可选，默认10）
     * @return 分页商品销量统计数据
     */
    @GetMapping("/products")
    public Result<PageBean<ProductSalesStatsDTO>> getProductSalesStats(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, defaultValue = "sales") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        // 构建查询参数
        SalesStatsQueryDTO queryDTO = new SalesStatsQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortOrder(sortOrder);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        return salesStatsService.getProductSalesStats(queryDTO);
    }

    /**
     * 获取类别销量统计
     * @param startDate 开始日期（可选，格式：yyyy-MM-dd）
     * @param endDate 结束日期（可选，格式：yyyy-MM-dd）
     * @return 类别销量统计列表
     */
    @GetMapping("/categories")
    public Result<List<CategorySalesStatsDTO>> getCategorySalesStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        // 构建查询参数
        SalesStatsQueryDTO queryDTO = new SalesStatsQueryDTO();
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);
        
        return salesStatsService.getCategorySalesStats(queryDTO);
    }

    /**
     * 获取总体销量统计数据
     * @param startDate 开始日期（可选，格式：yyyy-MM-dd）
     * @param endDate 结束日期（可选，格式：yyyy-MM-dd）
     * @return 包含总销量、总金额和商品数的统计信息
     */
    @GetMapping("/overall")
    public Result<Map<String, Object>> getOverallSalesStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        // 构建查询参数
        SalesStatsQueryDTO queryDTO = new SalesStatsQueryDTO();
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);
        
        return salesStatsService.getOverallSalesStats(queryDTO);
    }
} 