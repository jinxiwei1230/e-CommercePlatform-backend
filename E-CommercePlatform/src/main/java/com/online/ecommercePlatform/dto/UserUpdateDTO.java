package com.online.ecommercePlatform.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户信息更新数据传输对象
 */
@Data
public class UserUpdateDTO {
    
    private Long userId;
    
    @Size(min = 5, max = 16, message = "用户名长度必须在5-16位之间")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    private String gender;
    
    private String address;
} 