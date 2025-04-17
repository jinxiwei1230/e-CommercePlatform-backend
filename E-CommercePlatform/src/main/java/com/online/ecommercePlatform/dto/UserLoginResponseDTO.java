package com.online.ecommercePlatform.dto;

import lombok.Data;
import java.util.Map;

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
    private Object userInfo;
    
    /**
     * 构造函数
     * @param token JWT令牌
     * @param userInfo 用户信息
     */
    public UserLoginResponseDTO(String token, Object userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }
} 