package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            if (loggedInUser != null) {
                return Result.success(loggedInUser);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 新增用户
     * @param user 用户对象，包含用户名、密码等信息
     * @return 注册结果
     */
    @PostMapping("/add")
    public Result<User> add(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return Result.success(registeredUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 根据用户 ID 获取用户信息
     * @param id 用户 ID
     * @return 用户信息查询结果
     */
    @GetMapping("/selectById/{id}")
    public Result<User> selectById(@PathVariable Long id) {
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
     * @param user 更新后的用户对象
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<User> update(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        try {
            boolean result = userService.deleteUser(id);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除失败，用户可能不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 删除结果
     */
    @DeleteMapping("/delete/batch")
    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
        try {
            int count = userService.deleteUserBatch(ids);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    @GetMapping("/selectAll")
    public Result<List<User>> selectAll(User user) {
        try {
            List<User> users = userService.getAllUsers();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询用户
     * @param user 查询条件，包含用户名、角色、邮箱等筛选条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping("/selectPage")
    public Result<PageBean<User>> selectPage(
            User user,
            @RequestParam(defaultValue = "1") int pageNum, 
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageBean<User> pageBean = userService.getUsersByPage(pageNum, pageSize, user);
            return Result.success(pageBean);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}