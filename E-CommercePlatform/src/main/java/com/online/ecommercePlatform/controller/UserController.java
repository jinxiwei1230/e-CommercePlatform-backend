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
    @GetMapping("/selectById/current")
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

    /**
     * 更新用户信息
     * @param user 更新后的用户对象
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody User user) {
        // 确保密码字段不被更新
        user.setPassword(null);
        User updatedUser = userService.updateUser(user);
        return Result.success("用户信息更新成功", updatedUser);
        // 错误或者异常被全局异常类GlobalExceptionHandler捕获
    }

    /**
     * 更新用户密码
     */
    @PutMapping("/updatePassword")
    public Result<User> updatePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PasswordUpdateDTO passwordUpdateRequest) {

        // 1. 从请求头中获取 Token
        String token = authHeader;
        if (!StringUtils.hasText(token)) {
            return Result.error(401,"未登录");
        }

        // 2. 去掉 "Bearer " 前缀（如果存在）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 3. 验证 Token 并获取用户ID
        try {
            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null || jwtUtil.isTokenExpired(token)) {
                return Result.error(403,"登录已过期或Token无效");
            }

            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error(422,"Token中缺少用户信息");
            }

            Long userId = Long.valueOf(userIdStr);

            // 4. 调用 Service 更新密码
            User updatedUser = userService.updatePassword(
                    userId,
                    passwordUpdateRequest.getOldPassword(),
                    passwordUpdateRequest.getNewPassword()
            );
            return Result.success("密码更新成功", updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(403,"登录已过期或Token无效");
        }
    }


    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        return Result.success("用户删除成功", result);
    }

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 删除结果
     */
    @DeleteMapping("/delete/batch")
    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
        int count = userService.deleteUserBatch(ids);
        return Result.success("批量删除成功，删除了 " + count + " 个用户", count);
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @GetMapping("/selectAll")
    public Result<List<User>> selectAll(User user) {
        List<User> users = userService.getAllUsers();
        return Result.success("查询所有用户成功", users);
    }


}