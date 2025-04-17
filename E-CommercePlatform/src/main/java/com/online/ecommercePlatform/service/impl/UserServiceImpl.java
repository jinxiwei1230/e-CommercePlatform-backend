package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserLoginResponseDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
import com.online.ecommercePlatform.dto.UserUpdateDTO;
import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.UserService;
import com.online.ecommercePlatform.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        
        // 创建用户返回信息（只保留四个指定字段）
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
        
        
        // 6. 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        // 7. 执行更新操作
        userMapper.update(user);
        
        // 8. 清除敏感信息
        user.setPassword(null);
        
        // 9. 返回更新后的用户信息
        return Result.success(user);
    }
}
