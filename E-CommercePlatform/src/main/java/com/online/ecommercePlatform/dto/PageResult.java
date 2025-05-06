package com.online.ecommercePlatform.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据列表的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;    // 总记录数
    private Integer page;      // 当前页码
    private Integer pageSize;  // 每页数量
    private List<T> records; // 当前页数据列表
} 