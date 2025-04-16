package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
import com.online.ecommercePlatform.pojo.User;

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
     * @return 登录结果，成功时包含JWT令牌和用户信息
     */
    Result<UserLoginResponseDTO> login(UserLoginDTO loginDTO);
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    Result<?> getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     * @param updateDTO 用户更新信息
     * @return 更新结果，包含更新后的用户信息
     */
    Result<User> updateUserInfo(UserUpdateDTO updateDTO);
    
    /**
     * 验证码校验
     * @param email 邮箱
     * @param code 验证码
     * @return 校验结果
     */
    boolean verifyCode(String email, String code);
}
