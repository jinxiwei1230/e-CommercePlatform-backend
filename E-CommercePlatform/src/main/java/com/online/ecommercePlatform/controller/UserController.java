package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器，处理用户相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService; // 用户服务，用于处理用户业务逻辑

    /**
     * 用户注册接口
     * @param user 用户对象，包含用户名、密码等信息
     * @return 注册成功的用户对象
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 用户登录接口
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     */
    @PostMapping("/login")
    public User login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    /**
     * 根据用户 ID 获取用户信息
     * @param id 用户 ID
     * @return 对应的用户对象
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 更新用户信息
     * @param id 用户 ID
     * @param user 更新后的用户对象
     * @return 更新后的用户对象
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setUserId(id);
        return userService.updateUser(user);
    }
}
