package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.dto.UserListDTO;
import com.online.ecommercePlatform.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    int deleteById(Long id);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 删除结果
     */
    int deleteBatch(List<Long> ids);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 分页查询用户
     * @return
     */
    List<User> findByPage();

    /**
     * 带条件的分页查询用户
     * @param user
     * @return
     */
    List<User> findByPageWithCondition(User user);

    /**
     * 分页查询用户列表，支持多条件筛选和排序
     * @param username 用户名（模糊搜索）
     * @param isVip VIP状态
     * @param role 用户角色
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 用户列表
     */
    List<UserListDTO> listUsers(
            @Param("username") String username,
            @Param("isVip") Boolean isVip,
            @Param("role") String role,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder
    );
    
    /**
     * 统计满足条件的用户总数
     * @param username 用户名（模糊搜索）
     * @param isVip VIP状态
     * @param role 用户角色
     * @return 用户总数
     */
    Long countUsers(
            @Param("username") String username,
            @Param("isVip") Boolean isVip,
            @Param("role") String role
    );
    
    /**
     * 获取用户消费总金额
     * @param userId 用户ID
     * @return 消费总金额
     */
    BigDecimal getUserTotalSpent(@Param("userId") Long userId);
}
