package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.PasswordUpdateDTO;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 处理用户注册、登录等请求
 */
@RestController
@RequestMapping("/api/user")
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
     * @param request 用于获取用户认证信息的请求对象
     * @return 更新结果，包含更新后的用户信息
     */
    @PutMapping("/update")
    public Result<User> updateUserInfo(@Valid @RequestBody UserUpdateDTO updateDTO, HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED);
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
                return Result.error(Result.TOKEN_EXPIRED);
            }

            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error(Result.TOKEN_EXPIRED);
            }
            
            // 从token中获取userId并设置到DTO中
            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error(Result.BAD_REQUEST);
            }
            
            Long tokenUserId = Long.valueOf(userIdStr);
            Long updateUserId = updateDTO.getUserId();
            
            // 验证操作权限（只能更新自己的信息，管理员除外）
            if (!tokenUserId.equals(updateUserId)) {
                // 这里可以加入管理员权限判断逻辑
                return Result.error(Result.UNAUTHORIZED);
            }
            
            // 执行更新操作
            return userService.updateUserInfo(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.TOKEN_EXPIRED);
        }
    }
    
    /**
     * 获取当前登录用户信息
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/selectById/current")
    public Result<?> getUserInfo(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED);
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
                return Result.error(Result.TOKEN_EXPIRED);
            }
            
            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error(Result.TOKEN_EXPIRED);
            }
            
            // 获取用户ID
            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error(Result.BAD_REQUEST);
            }
            
            // 获取用户信息
            Long userId = Long.valueOf(userIdStr);
            return userService.getUserInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.TOKEN_EXPIRED);
        }
    }
    
    /**
     * 更新用户密码
     * @param passwordUpdateDTO 密码更新DTO
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/updatePassword")
    public Result<User> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO, HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return Result.error(Result.UNAUTHORIZED, "未提供认证令牌");
        }

        // 解析token获取用户信息
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
            
            // 从token中获取userId
            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error(Result.BAD_REQUEST, "无法获取用户ID");
            }
            
            Long userId = Long.valueOf(userIdStr);
            
            // 执行密码更新操作
            return userService.updatePassword(userId, passwordUpdateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.UNAUTHORIZED, "认证失败");
        }
    }
}