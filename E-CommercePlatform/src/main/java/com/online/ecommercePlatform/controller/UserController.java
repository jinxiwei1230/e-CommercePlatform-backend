package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器，处理用户相关的 HTTP 请求
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService; // 用户服务，用于处理用户业务逻辑

    /**
     * 用户注册接口
     * @param user 用户对象，包含用户名、密码等信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return Result.success(registeredUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录接口
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        try {
            User loggedInUser = userService.login(username, password);
            return Result.success(loggedInUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据用户 ID 获取用户信息
     * @param id 用户 ID
     * @return 用户信息查询结果
     */
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param id 用户 ID
     * @param user 更新后的用户对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setUserId(id);
            User updatedUser = userService.updateUser(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}