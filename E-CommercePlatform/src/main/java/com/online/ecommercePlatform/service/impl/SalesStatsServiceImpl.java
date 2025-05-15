package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.online.ecommercePlatform.dto.CategorySalesStatsDTO;
import com.online.ecommercePlatform.dto.ProductSalesStatsDTO;
import com.online.ecommercePlatform.dto.SalesStatsQueryDTO;
import com.online.ecommercePlatform.dto.TopCategorySalesStatsDTO;
import com.online.ecommercePlatform.mapper.SalesStatsMapper;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.SalesStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 销量统计服务实现类
 */
@Service
public class SalesStatsServiceImpl implements SalesStatsService {

    @Autowired
    private SalesStatsMapper salesStatsMapper;

    /**
     * 获取商品销量统计
     * @param queryDTO 查询参数
     * @return 分页商品销量统计数据
     */
    @Override
    public Result<PageBean<ProductSalesStatsDTO>> getProductSalesStats(SalesStatsQueryDTO queryDTO) {
        try {
            // 参数检查和默认值设置
            if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
                queryDTO.setPageNum(1);
            }
            if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
                queryDTO.setPageSize(10);
            }
            
            // 排序参数处理，默认按销量降序排序
            if (queryDTO.getSortBy() == null || queryDTO.getSortBy().isEmpty() || 
                (!queryDTO.getSortBy().equals("sales") && !queryDTO.getSortBy().equals("amount"))) {
                queryDTO.setSortBy("sales");
            }
            if (queryDTO.getSortOrder() == null || queryDTO.getSortOrder().isEmpty() || 
                (!queryDTO.getSortOrder().equals("asc") && !queryDTO.getSortOrder().equals("desc"))) {
                queryDTO.setSortOrder("desc");
            }

            // 设置分页
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
            
            // 查询数据
            List<ProductSalesStatsDTO> statsList = salesStatsMapper.selectProductSalesStats(
                    queryDTO.isAllCategories() ? null : queryDTO.getCategoryId(),
                    queryDTO.getStartDate(),
                    queryDTO.getEndDate(),
                    queryDTO.getSortBy(),
                    queryDTO.getSortOrder()
            );

            // 查询总数
            int total = salesStatsMapper.countProductSalesStats(
                    queryDTO.isAllCategories() ? null : queryDTO.getCategoryId(),
                    queryDTO.getStartDate(),
                    queryDTO.getEndDate()
            );

            // 封装分页结果
            PageBean<ProductSalesStatsDTO> pageBean = new PageBean<>();
            pageBean.setTotal((long) total);
            pageBean.setItems(statsList);

            return Result.success(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "获取商品销量统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取类别销量统计
     * @param queryDTO 查询参数
     * @return 类别销量统计列表
     */
    @Override
    public Result<List<CategorySalesStatsDTO>> getCategorySalesStats(SalesStatsQueryDTO queryDTO) {
        try {
            // 查询类别销量统计数据
            List<CategorySalesStatsDTO> statsList = salesStatsMapper.selectCategorySalesStats(
                    queryDTO.getStartDate(),
                    queryDTO.getEndDate()
            );
            
            return Result.success(statsList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "获取类别销量统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取总体销量统计数据
     * @param queryDTO 查询参数（主要使用时间范围）
     * @return 包含总销量、总金额和商品数的统计信息
     */
    @Override
    public Result<Map<String, Object>> getOverallSalesStats(SalesStatsQueryDTO queryDTO) {
        try {
            // 查询总体销量统计数据
            Map<String, Object> statsMap = salesStatsMapper.selectOverallSalesStats(
                    queryDTO.getStartDate(),
                    queryDTO.getEndDate()
            );
            
            return Result.success(statsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.SERVER_ERROR, "获取总体销量统计失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<TopCategorySalesStatsDTO>> getTopCategorySalesStats(SalesStatsQueryDTO queryDTO) {
        try {
            List<TopCategorySalesStatsDTO> stats = salesStatsMapper.getTopCategorySalesStats(
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
            );
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取顶级类别销量统计失败：" + e.getMessage());
        }
    }
} 