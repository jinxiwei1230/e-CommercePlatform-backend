package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志数据访问接口
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 插入操作日志
     * @param log 日志对象
     * @return 影响的行数
     */
    int insert(OperationLog log);
} 