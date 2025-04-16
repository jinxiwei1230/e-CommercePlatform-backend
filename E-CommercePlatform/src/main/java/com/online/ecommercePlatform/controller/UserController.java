package com.online.ecommercePlatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.pojo.PageBean;
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
     * @return 登录结果，包含JWT令牌
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
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
            return Result.error("未登录");
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
                return Result.error("登录已过期或Token无效");
            }
            
            // 检查令牌是否过期
            if (jwtUtil.isTokenExpired(token)) {
                return Result.error("登录已过期，请重新登录");
            }
            
            // 获取用户ID
            String userIdStr = jwt.getClaim("userId").asString();
            if (!StringUtils.hasText(userIdStr)) {
                return Result.error("Token中缺少用户信息");
            }
            
            // 获取用户信息
            Long userId = Long.valueOf(userIdStr);
            return userService.getUserInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录已过期或Token无效");
        }
    }

//    /**
//     * 更新用户信息
//     * @param user 更新后的用户对象
//     * @return 更新结果
//     */
//    @PutMapping("/update")
//    public Result<User> update(@RequestBody User user) {
//        try {
//            User updatedUser = userService.updateUser(user);
//            return Result.success(updatedUser);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 删除用户
//     * @param id 用户ID
//     * @return 删除结果
//     */
//    @DeleteMapping("/delete/{id}")
//    public Result<Boolean> delete(@PathVariable Long id) {
//        try {
//            boolean result = userService.deleteUser(id);
//            if (result) {
//                return Result.success(true);
//            } else {
//                return Result.error("删除失败，用户可能不存在");
//            }
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 批量删除用户
//     * @param ids 用户ID列表
//     * @return 删除结果
//     */
//    @DeleteMapping("/delete/batch")
//    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
//        try {
//            int count = userService.deleteUserBatch(ids);
//            return Result.success(count);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 查询所有用户
//     * @return 用户列表
//     */
//    @GetMapping("/selectAll")
//    public Result<List<User>> selectAll(User user) {
//        try {
//            List<User> users = userService.getAllUsers();
//            return Result.success(users);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 分页查询用户
//     * @param user 查询条件，包含用户名、角色、邮箱等筛选条件
//     * @param pageNum 页码
//     * @param pageSize 每页条数
//     * @return 分页结果
//     */
//    @GetMapping("/selectPage")
//    public Result<PageBean<User>> selectPage(
//            User user,
//            @RequestParam(defaultValue = "1") int pageNum,
//            @RequestParam(defaultValue = "10") int pageSize) {
//        try {
//            PageBean<User> pageBean = userService.getUsersByPage(pageNum, pageSize, user);
//            return Result.success(pageBean);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }

}