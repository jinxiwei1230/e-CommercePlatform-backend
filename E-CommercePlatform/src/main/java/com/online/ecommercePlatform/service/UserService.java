package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.PasswordUpdateDTO;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.dto.UserQueryDTO;
import com.online.ecommercePlatform.dto.PageResult;
import com.online.ecommercePlatform.dto.UserListDTO;
import com.online.ecommercePlatform.dto.AdminUserUpdateDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
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
     * 更新用户密码
     * @param userId 用户ID
     * @param passwordUpdateDTO 密码更新DTO，包含旧密码和新密码
     * @return 更新结果
     */
    Result<User> updatePassword(Long userId, PasswordUpdateDTO passwordUpdateDTO);
    
    /**
     * 验证码校验
     * @param email 邮箱
     * @param code 验证码
     * @return 校验结果
     */

    /**
     * 获取用户列表
     * @param queryDTO 查询参数
     * @return 包含用户列表的分页结果
     */
    Result<PageResult<UserListDTO>> listUsers(UserQueryDTO queryDTO);

    /**
     * 管理员更新用户信息
     * @param adminUserUpdateDTO 用户更新信息
     * @param adminId 管理员ID
     * @return 更新结果
     */
    Result<User> adminUpdateUserInfo(AdminUserUpdateDTO adminUserUpdateDTO, Long adminId);

    /**
     * 管理员根据userId查询用户信息
     * @param userId 要查询的用户ID
     * @param adminId 管理员ID
     * @return 查询结果
     */
    Result<?> adminGetUserInfo(Long userId, Long adminId);
}
