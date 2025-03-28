package com.online.ecommercePlatform.service;
import com.online.ecommercePlatform.pojo.User;

/**
 * 用户服务接口，定义用户相关的业务逻辑
 */
public interface UserService {

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册成功的用户对象
     */
    User register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     */
    User login(String username, String password);

    /**
     * 通过用户 ID 获取用户信息
     * @param id 用户 ID
     * @return 用户对象
     */
    User getUserById(Long id);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    User updateUser(User user);
}
