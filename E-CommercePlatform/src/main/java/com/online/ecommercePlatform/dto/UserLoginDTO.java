package com.online.ecommercePlatform.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户登录数据传输对象
 */
@Data
public class UserLoginDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 16, message = "用户名长度必须在5-16位之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码长度必须在6-18位之间")
    private String password;
    
    @Pattern(regexp = "^(password|wechat|alipay)$", message = "登录类型不正确")
    private String loginType = "password"; // 默认密码登录
} 