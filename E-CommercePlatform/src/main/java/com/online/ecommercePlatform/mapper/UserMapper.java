package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问层接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象，未找到返回null
     */
    User findById(Long userId);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，未找到返回null
     */
    User findByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户对象，未找到返回null
     */
    User findByEmail(String email);
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象，未找到返回null
     */
    User findByPhone(String phone);
    
    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响的行数
     */
    int update(User user);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);
}
