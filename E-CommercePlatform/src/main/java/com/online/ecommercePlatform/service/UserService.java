package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.User;

import java.util.List;

/**
 * 用户服务接口
 * 定义用户相关的业务操作
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    Result<?> register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果，成功时包含JWT令牌
     */
    Result<String> login(UserLoginDTO loginDTO);
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    Result<?> getUserInfo(Long userId);
    
    /**
     * 验证码校验
     * @param email 邮箱
     * @param code 验证码
     * @return 校验结果
     */
    boolean verifyCode(String email, String code);

    /**
     * 更新用户信息
     * @param user 更新后的用户对象
     * @return 更新结果
     */
    User updateUser(User user);

    /**
     * 更新用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新结果
     */
    User updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long id);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 删除结果
     */
    int deleteUserBatch(List<Long> ids);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();

}
