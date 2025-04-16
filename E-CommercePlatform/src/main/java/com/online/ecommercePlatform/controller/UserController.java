package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户注册、登录等请求
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册接口
     * @param registerDTO 注册信息DTO
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }
    
    /**
     * 用户登录接口
     * @param loginDTO 登录信息DTO
     * @return 登录结果，包含JWT令牌和用户信息
     */
    @PostMapping("/login")
    public Result<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
    
    /**
     * 更新用户信息接口
     * @param updateDTO 用户更新信息DTO
     * @return 更新结果，包含更新后的用户信息
     */
    @PutMapping("/update")
    public Result<User> updateUserInfo(@Valid @RequestBody UserUpdateDTO updateDTO, HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED, "未登录");
        }
        
        // 解析token获取用户名
        try {
            // 如果token以Bearer 开头，需要去掉前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 验证Token
            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null) {
                return Result.error(Result.TOKEN_EXPIRED, "登录已过期或Token无效");
            }
            
            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error(Result.TOKEN_EXPIRED, "登录已过期，请重新登录");
            }
            
            // 获取用户名并验证权限
            String tokenUsername = jwt.getClaim("username").asString();
            if (!StringUtils.hasText(tokenUsername)) {
                return Result.error(Result.BAD_REQUEST, "Token中缺少用户信息");
            }
            
            String updateUsername = updateDTO.getUsername();
            
            // 验证操作权限（只能更新自己的信息，管理员除外）
            if (!tokenUsername.equals(updateUsername)) {
                // 这里可以加入管理员权限判断逻辑
                return Result.error(Result.UNAUTHORIZED, "无权更新他人信息");
            }
            
            // 从token中获取userId并设置到DTO中
            String userIdStr = jwt.getClaim("userId").asString();
            if (StringUtils.hasText(userIdStr)) {
                updateDTO.setUserId(Long.valueOf(userIdStr));
            } else {
                return Result.error(Result.BAD_REQUEST, "Token中缺少用户ID信息");
            }
            
            // 执行更新操作
            return userService.updateUserInfo(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.TOKEN_EXPIRED, "登录已过期或Token无效");
        }
    }
    
    /**
     * 获取当前登录用户信息
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED, "未登录");
        }
        
        // 解析token获取用户ID
        try {
            // 如果token以Bearer 开头，需要去掉前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 验证Token
            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null) {
                return Result.error(Result.TOKEN_EXPIRED, "登录已过期或Token无效");
            }
            
            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error(Result.TOKEN_EXPIRED, "登录已过期，请重新登录");
            }
            
            // 获取用户ID
            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error(Result.BAD_REQUEST, "Token中缺少用户信息");
            }
            
            // 获取用户信息
            Long userId = Long.valueOf(userIdStr);
            return userService.getUserInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.TOKEN_EXPIRED, "登录已过期或Token无效");
        }
    }
}