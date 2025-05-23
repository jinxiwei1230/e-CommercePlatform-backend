package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
import com.online.ecommercePlatform.dto.PasswordUpdateDTO;
import com.online.ecommercePlatform.dto.UserQueryDTO;
import com.online.ecommercePlatform.dto.PageResult;
import com.online.ecommercePlatform.dto.UserListDTO;
import com.online.ecommercePlatform.dto.AdminUserUpdateDTO;
import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import com.online.ecommercePlatform.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 用户注册
     */
    @Override
    public Result<?> register(UserRegisterDTO registerDTO) {
        // 1. 参数校验
        String email = registerDTO.getEmail();
        String phone = registerDTO.getPhone();
        
        // 验证邮箱或手机号至少有一个
        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone)) {
            return Result.error(Result.EMAIL_PHONE_REQUIRED);
        }
        
        // 2. 检查用户名是否已存在
        if (userMapper.existsByUsername(registerDTO.getUsername())) {
            return Result.error(Result.USERNAME_EXISTS);
        }
        
        // 3. 检查邮箱是否已存在
        if (StringUtils.hasText(email) && userMapper.existsByEmail(email)) {
            return Result.error(Result.EMAIL_EXISTS);
        }
        
        // 4. 检查手机号是否已存在
        if (StringUtils.hasText(phone) && userMapper.existsByPhone(phone)) {
            return Result.error(Result.PHONE_EXISTS);
        }
        
        // 5. 密码加密 - 修改为使用BCrypt
        String encryptedPassword = passwordEncoder.encode(registerDTO.getPassword());
        
        // 6. 创建用户对象
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(registerDTO.getGender());
        user.setIsVip(false);
        user.setRole(registerDTO.getRole());
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 7. 插入用户数据
        userMapper.insert(user);
        
        // 8. 获取插入后的用户(包含userId)
        User createdUser = userMapper.findByUsername(registerDTO.getUsername());
        
        // 9. 清除敏感信息
        createdUser.setPassword(null);
        
        // 10. 返回简化的用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", createdUser.getUserId());
        userMap.put("username", createdUser.getUsername());
        userMap.put("email", createdUser.getEmail());
        userMap.put("phone", createdUser.getPhone());
        
        return Result.success(userMap);
    }

    /**
     * 用户登录
     */
    @Override
    public Result<UserLoginResponseDTO> login(UserLoginDTO loginDTO) {
        // 获取登录信息
        String username = loginDTO.getUsername();
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        
        // 验证至少提供了用户名或手机号
        if (!StringUtils.hasText(username) && !StringUtils.hasText(phone)) {
            return Result.error(Result.BAD_REQUEST);
        }
        
        // 根据提供的登录方式查询用户
        User user = null;
        if (StringUtils.hasText(username)) {
            // 使用用户名登录
            user = userMapper.findByUsername(username);
        } else if (StringUtils.hasText(phone)) {
            // 使用手机号登录
            user = userMapper.findByPhone(phone);
        }
        
        // 用户不存在
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED);
        }
        
        // 密码校验 - 使用BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error(Result.UNAUTHORIZED);
        }
        
        // 生成JWT令牌
        Map<String, String> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getUserId().toString());
        String token = jwtUtil.generateToken(claims);
        
        // 创建用户返回信息（只保留指定字段）
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getUserId());
        userMap.put("username", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("phone", user.getPhone());
        
        // 创建登录响应DTO
        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO(token, userMap);
        
        // 返回登录成功结果
        return Result.success(responseDTO);
    }
    
    /**
     * 获取用户信息
     */
    @Override
    public Result<?> getUserInfo(Long userId) {
        // 根据ID查询用户
        User user = userMapper.findById(userId);
        
        if (user == null) {
            return Result.error(Result.NOT_FOUND);
        }
        
        // 清除敏感信息
        user.setPassword(null);
        
        return Result.success(user);
    }
    
    /**
     * 更新用户信息
     */
    @Override
    public Result<User> updateUserInfo(UserUpdateDTO updateDTO) {
        // 1. 验证用户是否存在
        Long userId = updateDTO.getUserId();
        if (userId == null) {
            return Result.error(Result.BAD_REQUEST);
        }
        
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error(Result.NOT_FOUND);
        }
        
        // 2. 验证用户名是否已被其他用户使用
        String newUsername = updateDTO.getUsername();
        if (StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())) {
            if (userMapper.existsByUsername(newUsername)) {
                return Result.error(Result.USERNAME_EXISTS);
            }
            user.setUsername(newUsername);
        }
        
        // 3. 验证邮箱是否已被其他用户使用
        String newEmail = updateDTO.getEmail();
        if (StringUtils.hasText(newEmail) && !newEmail.equals(user.getEmail())) {
            if (userMapper.existsByEmail(newEmail)) {
                return Result.error(Result.EMAIL_EXISTS);
            }
            user.setEmail(newEmail);
        }
        
        // 4. 验证手机号是否已被其他用户使用
        String newPhone = updateDTO.getPhone();
        if (StringUtils.hasText(newPhone) && !newPhone.equals(user.getPhone())) {
            if (userMapper.existsByPhone(newPhone)) {
                return Result.error(Result.PHONE_EXISTS);
            }
            user.setPhone(newPhone);
        }
        
        // 5. 更新其他信息
        if (StringUtils.hasText(updateDTO.getGender())) {
            user.setGender(updateDTO.getGender());
        }
        
        if (updateDTO.getAge() != null) {
            user.setAge(updateDTO.getAge());
        }
        
        // 6. 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        // 7. 执行更新操作
        userMapper.update(user);
        
        // 8. 清除敏感信息
        user.setPassword(null);
        
        // 9. 返回更新后的用户信息
        return Result.success(user);
    }
    
    /**
     * 更新用户密码
     */
    @Override
    public Result<User> updatePassword(Long userId, PasswordUpdateDTO passwordUpdateDTO) {
        // 1. 参数校验
        String oldPassword = passwordUpdateDTO.getOldPassword();
        String newPassword = passwordUpdateDTO.getNewPassword();
        
        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return Result.error(Result.BAD_REQUEST, "旧密码和新密码不能为空");
        }
        
        // 2. 新密码长度校验
        if (newPassword.length() < 6) {
            return Result.error(422, "新密码长度至少6位");
        }
        
        // 3. 查询用户信息
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error(Result.NOT_FOUND, "用户不存在");
        }
        
        // 4. 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error(Result.UNAUTHORIZED, "原密码错误");
        }
        
        // 5. 检查新旧密码是否相同
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return Result.error(422, "新密码与旧密码不能相同");
        }
        
        // 6. 加密新密码
        String encryptedPassword = passwordEncoder.encode(newPassword);
        
        // 7. 更新用户密码
        user.setPassword(encryptedPassword);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        
        // 返回成功响应，并包含用户信息
        Result<User> result = Result.success(user);
        result.setMessage("密码更新成功");
        return result;
    }
    
    /**
     * 验证码校验
     */
    // ... existing code ...

    /**
     * 获取用户列表，支持多条件筛选和分页
     */
    @Override
    public Result<PageResult<UserListDTO>> listUsers(UserQueryDTO queryDTO) {
        // 参数校验和默认值设置
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }

        try {
            // 设置分页参数
            PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
            
            // 执行查询
            List<UserListDTO> userList = userMapper.listUsers(
                    queryDTO.getUsername(),
                    queryDTO.getIsVip(),
                    queryDTO.getRole(),
                    queryDTO.getSortBy(),
                    queryDTO.getSortOrder()
            );
            
            // 获取查询结果的分页信息
            PageInfo<UserListDTO> pageInfo = new PageInfo<>(userList);
            
            // 补充用户的购物总次数
            for (UserListDTO user : userList) {
                Integer totalOrders = userMapper.getUserTotalOrders(user.getUserId());
                user.setTotalSpent(totalOrders); // 变量名保持 totalSpent，但含义已变为总次数
            }
            
            // 构造分页结果
            PageResult<UserListDTO> pageResult = new PageResult<>();
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPage(queryDTO.getPage());
            pageResult.setPageSize(queryDTO.getPageSize());
            pageResult.setRecords(userList);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 管理员更新用户信息
     */
    @Override
    public Result<User> adminUpdateUserInfo(AdminUserUpdateDTO adminUserUpdateDTO, Long adminId) {
        // 1. 验证管理员权限
        User admin = userMapper.findById(adminId);
        if (admin == null || !"管理员".equals(admin.getRole())) {
            return Result.error(403, "无管理员权限");
        }
        
        // 2. 验证要修改的用户是否存在
        Long userId = adminUserUpdateDTO.getUserId();
        if (userId == null) {
            return Result.error(400, "用户ID不能为空");
        }
        
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        
        // 3. 验证用户名是否已被其他用户使用
        String newUsername = adminUserUpdateDTO.getUsername();
        if (StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())) {
            if (userMapper.existsByUsername(newUsername)) {
                return Result.error(400, "用户名已存在");
            }
            user.setUsername(newUsername);
        }
        
        // 4. 验证邮箱是否已被其他用户使用
        String newEmail = adminUserUpdateDTO.getEmail();
        if (StringUtils.hasText(newEmail) && !newEmail.equals(user.getEmail())) {
            if (userMapper.existsByEmail(newEmail)) {
                return Result.error(400, "邮箱已存在");
            }
            user.setEmail(newEmail);
        }
        
        // 5. 验证手机号是否已被其他用户使用
        String newPhone = adminUserUpdateDTO.getPhone();
        if (StringUtils.hasText(newPhone) && !newPhone.equals(user.getPhone())) {
            if (userMapper.existsByPhone(newPhone)) {
                return Result.error(400, "手机号已存在");
            }
            user.setPhone(newPhone);
        }
        
        // 6. 更新其他信息
        if (StringUtils.hasText(adminUserUpdateDTO.getGender())) {
            user.setGender(adminUserUpdateDTO.getGender());
        }
        
        if (adminUserUpdateDTO.getAge() != null) {
            user.setAge(adminUserUpdateDTO.getAge());
        }
        
        if (adminUserUpdateDTO.getIsVip() != null) {
            user.setIsVip(adminUserUpdateDTO.getIsVip());
        }
        
        if (StringUtils.hasText(adminUserUpdateDTO.getRole())) {
            user.setRole(adminUserUpdateDTO.getRole());
        }
        
        // 7. 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        // 8. 执行更新操作
        userMapper.update(user);
        
        // 9. 清除敏感信息
        user.setPassword(null);
        
        // 10. 返回更新后的用户信息
        return Result.success(user);
    }

    /**
     * 管理员根据userId查询用户信息
     */
    @Override
    public Result<?> adminGetUserInfo(Long userId, Long adminId) {
        // 1. 验证管理员权限
        User admin = userMapper.findById(adminId);
        if (admin == null || !"管理员".equals(admin.getRole())) {
            return Result.error(403, "无管理员权限");
        }
        
        // 2. 查询指定用户
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        
        // 3. 清除敏感信息
        user.setPassword(null);
        
        // 4. 返回用户信息
        return Result.success(user);
    }
}
