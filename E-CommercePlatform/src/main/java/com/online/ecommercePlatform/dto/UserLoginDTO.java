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

    /**
     * 用户名（可选）- 用户可以使用用户名或手机号登录
     */
    @Size(min = 5, max = 16, message = "用户名长度必须在5-16位之间")
    private String username;

    /**
     * 手机号（可选）- 用户可以使用用户名或手机号登录
     */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码长度必须在6-18位之间")
    private String password;

    /**
     * 登录类型：password-密码登录，wechat-微信登录，alipay-支付宝登录
     */
    @Pattern(regexp = "^(password|wechat|alipay)$", message = "登录类型不正确")
    private String loginType = "password"; // 默认密码登录
}