package com.online.ecommercePlatform.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 添加地址数据传输对象
 */
@Data
public class AddressAddDTO {
    
    @NotBlank(message = "收件人姓名不能为空")
    private String recipient_name;
    
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "详细地址不能为空")
    private String address_line1;
    
    @NotBlank(message = "城市不能为空")
    private String city;
    
    @NotBlank(message = "省份不能为空")
    private String state;
    
    @NotBlank(message = "邮政编码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postal_code;
    
    private Boolean is_default = false;
} 