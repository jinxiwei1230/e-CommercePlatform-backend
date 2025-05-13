package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.AddressAddDTO;
import com.online.ecommercePlatform.dto.AddressListResponseDTO;
import com.online.ecommercePlatform.dto.AddressUpdateDTO;
import com.online.ecommercePlatform.dto.PageDTO;
import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.AddressService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 地址管理控制器
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 获取当前用户的地址列表（分页）
     * @param pageDTO 分页参数
     * @param request HTTP请求，用于获取用户信息
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<AddressListResponseDTO> getAddressList(PageDTO pageDTO, HttpServletRequest request) {
        // 从Token中获取用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Result.error(Result.UNAUTHORIZED, "未登录或Token无效");
        }
        
        // 解析Token获取用户信息
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            return Result.error(Result.UNAUTHORIZED, "Token已过期或无效");
        }
        
        Long userId = Long.valueOf(jwt.getClaim("userId").asString());
        
        // 调用服务查询地址列表
        Result<PageBean<Address>> pageResult = addressService.getAddressList(userId, pageDTO);
        
        // 处理失败情况
        if (pageResult.getCode() != Result.SUCCESS) {
            return Result.error(pageResult.getCode(), pageResult.getMessage());
        }
        
        // 转换为前端需要的格式
        PageBean<Address> pageBean = pageResult.getData();
        AddressListResponseDTO responseDTO = AddressListResponseDTO.fromPageBean(
                pageBean.getTotal(),
                pageBean.getItems(),
                pageDTO.getPageSize(),
                pageDTO.getPage()
        );
        
        return Result.success(responseDTO);
    }
    
    /**
     * 添加新地址
     * @param addressAddDTO 地址信息
     * @param request HTTP请求，用于获取用户信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<?> addAddress(@Valid @RequestBody AddressAddDTO addressAddDTO, HttpServletRequest request) {
        // 从Token中获取用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Result.error(Result.UNAUTHORIZED, "未登录或Token无效");
        }
        
        // 解析Token获取用户信息
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            return Result.error(Result.UNAUTHORIZED, "Token已过期或无效");
        }
        
        Long userId = Long.valueOf(jwt.getClaim("userId").asString());
        
        // 调用服务添加地址
        Result<Long> result = addressService.addAddress(userId, addressAddDTO);
        
        // 处理失败情况
        if (result.getCode() != Result.SUCCESS) {
            return result;
        }
        
        // 组装符合前端预期的响应格式
        Map<String, Long> data = new HashMap<>();
        data.put("address_id", result.getData());
        
        return Result.success(data);
    }
    
    /**
     * 更新地址
     * @param addressUpdateDTO 地址更新信息
     * @param request HTTP请求，用于获取用户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<?> updateAddress(@Valid @RequestBody AddressUpdateDTO addressUpdateDTO, HttpServletRequest request) {
        // 从Token中获取用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Result.error(Result.UNAUTHORIZED, "未登录或Token无效");
        }
        
        // 解析Token获取用户信息
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            return Result.error(Result.UNAUTHORIZED, "Token已过期或无效");
        }
        
        Long userId = Long.valueOf(jwt.getClaim("userId").asString());
        
        // 调用服务更新地址
        Result<?> result = addressService.updateAddress(userId, addressUpdateDTO);
        
        return result;
    }
    
    /**
     * 删除地址
     * @param addressId 地址ID
     * @param request HTTP请求，用于获取用户信息
     * @return 删除结果
     */
    @DeleteMapping("/delete/{addressId}")
    public Result<?> deleteAddress(@PathVariable Long addressId, HttpServletRequest request) {
        // 从Token中获取用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Result.error(Result.UNAUTHORIZED, "未登录或Token无效");
        }
        
        // 解析Token获取用户信息
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            return Result.error(Result.UNAUTHORIZED, "Token已过期或无效");
        }
        
        Long userId = Long.valueOf(jwt.getClaim("userId").asString());
        
        // 调用服务删除地址
        Result<?> result = addressService.deleteAddress(userId, addressId);
        
        return result;
    }
    
    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @param request HTTP请求，用于获取用户信息
     * @return 设置结果
     */
    @PutMapping("/setDefault/{addressId}")
    public Result<?> setDefaultAddress(@PathVariable Long addressId, HttpServletRequest request) {
        // 从Token中获取用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Result.error(Result.UNAUTHORIZED, "未登录或Token无效");
        }
        
        // 解析Token获取用户信息
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            return Result.error(Result.UNAUTHORIZED, "Token已过期或无效");
        }
        
        Long userId = Long.valueOf(jwt.getClaim("userId").asString());
        
        // 调用服务设置默认地址
        Result<?> result = addressService.setDefaultAddress(userId, addressId);
        
        return result;
    }
    
    /**
     * 管理员获取指定用户的地址列表（分页）
     * @param userId 要查询的用户ID
     * @param pageDTO 分页参数
     * @param request HTTP请求
     * @return 地址列表分页结果
     */
    @GetMapping("/admin/{userId}")
    public Result<AddressListResponseDTO> adminGetAddressList(
            @PathVariable Long userId,
            PageDTO pageDTO,
            HttpServletRequest request) {
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED, "未提供认证令牌");
        }

        // 解析token获取管理员信息
        try {
            // 如果token以Bearer 开头，需要去掉前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 验证Token
            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null) {
                return Result.error(Result.UNAUTHORIZED, "认证令牌无效");
            }

            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error(Result.UNAUTHORIZED, "认证令牌已过期");
            }
            
            // 从token中获取管理员ID
            String adminIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(adminIdStr)) {
                return Result.error(Result.BAD_REQUEST, "无法获取用户ID");
            }
            
            Long adminId = Long.valueOf(adminIdStr);
            
            // 调用服务查询地址列表
            Result<PageBean<Address>> pageResult = addressService.adminGetAddressList(userId, adminId, pageDTO);
            
            // 处理失败情况
            if (pageResult.getCode() != Result.SUCCESS) {
                return Result.error(pageResult.getCode(), pageResult.getMessage());
            }
            
            // 转换为前端需要的格式
            PageBean<Address> pageBean = pageResult.getData();
            AddressListResponseDTO responseDTO = AddressListResponseDTO.fromPageBean(
                    pageBean.getTotal(),
                    pageBean.getItems(),
                    pageDTO.getPageSize(),
                    pageDTO.getPage()
            );
            
            return Result.success(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.UNAUTHORIZED, "认证失败: " + e.getMessage());
        }
    }
} 