package com.online.ecommercePlatform.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户注册数据传输对象
 */
@Data
public class UserRegisterDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 16, message = "用户名长度必须在5-16位之间")
    @Pattern(regexp = "^\\S+$", message = "用户名不能包含空白字符")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码长度必须在6-18位之间")
    @Pattern(regexp = "^\\S+$", message = "密码不能包含空白字符")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须是6位")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "验证码只能包含字母和数字")
    private String verifyCode;
} 