package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.pojo.Product;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.ProductService;
import com.online.ecommercePlatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员控制器，处理管理员对产品的管理操作
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService; // 注入 UserService

    /**
     * 管理员登录接口
     * @param loginDTO 登录信息DTO
     * @return 登录结果，包含JWT令牌和用户信息
     */
    @PostMapping("/login")
    public Result<?> adminLogin(@Valid @RequestBody UserLoginDTO loginDTO) {
        Result<UserLoginResponseDTO> loginResult = userService.login(loginDTO);

        // 检查登录是否成功
        if (loginResult.getCode() != Result.SUCCESS) {
            return loginResult; // 返回登录失败的错误信息
        }

        // 登录成功，获取 userId
        Long userId = null;
        UserLoginResponseDTO loginResponse = loginResult.getData();
        if (loginResponse.getUserInfo() instanceof Map) {
            Map<String, Object> userInfoMap = (Map<String, Object>) loginResponse.getUserInfo();
            Object userIdObj = userInfoMap.get("userId");
            if (userIdObj instanceof Number) { // 检查 userId 类型
                userId = ((Number) userIdObj).longValue();
            } else {
                 return Result.error(Result.BAD_REQUEST, "无法获取用户ID");
            }
        } else {
            return Result.error(500, "登录响应格式错误");
        }

        if (userId == null) {
             return Result.error(Result.BAD_REQUEST, "无法获取用户ID");
        }

        // 根据 userId 获取完整的用户信息
        Result<?> userInfoResult = userService.getUserInfo(userId);
        if (userInfoResult.getCode() != Result.SUCCESS || userInfoResult.getData() == null) {
            return Result.error(500, "获取用户信息失败");
        }

        // 检查角色
        User user = (User) userInfoResult.getData();
        if ("管理员".equals(user.getRole())) {
            // 角色正确，返回原始的登录成功结果（包含token和部分用户信息）
            return loginResult;
        } else {
            // 非管理员用户，返回权限不足错误
            return Result.error(Result.UNAUTHORIZED, "您没有权限登录后台管理系统");
        }
    }

}