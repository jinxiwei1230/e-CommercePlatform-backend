package com.online.ecommercePlatform.dto;

import com.online.ecommercePlatform.pojo.User;
import lombok.Data;

/**
 * 用户登录响应数据传输对象
 */
@Data
public class UserLoginResponseDTO {
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private User userInfo;
    
    /**
     * 构造函数
     * @param token JWT令牌
     * @param userInfo 用户信息
     */
    public UserLoginResponseDTO(String token, User userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }
} 