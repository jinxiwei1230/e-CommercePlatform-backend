package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类，处理用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper 以操作数据库

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册成功的用户对象
     */
    @Override
    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象，失败返回 null
     */
    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 通过用户 ID 获取用户信息
     * @param id 用户 ID
     * @return 用户对象
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    @Override
    public User updateUser(User user) {
        userMapper.update(user);
        return user;
    }
}
