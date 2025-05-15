package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.CategorySalesStatsDTO;
import com.online.ecommercePlatform.dto.ProductSalesStatsDTO;
import com.online.ecommercePlatform.dto.TopCategorySalesStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 销量统计数据访问层接口
 */
@Mapper
public interface SalesStatsMapper {

    /**
     * 获取商品销量统计列表
     * @param categoryId 分类ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 商品销量统计列表
     */
    List<ProductSalesStatsDTO> selectProductSalesStats(
            @Param("categoryId") Long categoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder);

    /**
     * 统计商品销量数据总数
     * @param categoryId 分类ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 商品销量数据总数
     */
    int countProductSalesStats(
            @Param("categoryId") Long categoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * 获取类别销量统计列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 类别销量统计列表
     */
    List<CategorySalesStatsDTO> selectCategorySalesStats(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * 获取总体销量统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 包含总销量、总金额和商品数的统计信息
     */
    Map<String, Object> selectOverallSalesStats(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * 获取顶级类别销量统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 顶级类别销量统计列表
     */
    List<TopCategorySalesStatsDTO> getTopCategorySalesStats(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
} 