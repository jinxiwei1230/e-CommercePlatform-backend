package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层接口，定义对用户表的基本操作
 */
@Mapper
public interface UserMapper {

    /**
     * 通过用户 ID 查找用户
     * @param id 用户 ID
     * @return 用户对象
     */
    User findById(Long id);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);

    /**
     * 插入新的用户
     * @param user 用户对象
     */
    void insert(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     */
    void update(User user);
}
