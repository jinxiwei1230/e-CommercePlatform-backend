package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.CategorySalesStatsDTO;
import com.online.ecommercePlatform.dto.ProductSalesStatsDTO;
import com.online.ecommercePlatform.dto.SalesStatsQueryDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;

import java.util.List;
import java.util.Map;

/**
 * 销量统计服务接口
 */
public interface SalesStatsService {

    /**
     * 获取商品销量统计（支持分页、排序和筛选）
     * @param queryDTO 查询参数
     * @return 分页商品销量统计数据
     */
    Result<PageBean<ProductSalesStatsDTO>> getProductSalesStats(SalesStatsQueryDTO queryDTO);
    
    /**
     * 获取类别销量统计
     * @param queryDTO 查询参数
     * @return 类别销量统计列表
     */
    Result<List<CategorySalesStatsDTO>> getCategorySalesStats(SalesStatsQueryDTO queryDTO);
    
    /**
     * 获取总体销量统计数据
     * @param queryDTO 查询参数（主要使用时间范围）
     * @return 包含总销量、总金额和商品数的统计信息
     */
    Result<Map<String, Object>> getOverallSalesStats(SalesStatsQueryDTO queryDTO);
} 