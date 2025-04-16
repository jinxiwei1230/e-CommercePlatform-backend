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
    
    // 验证码前缀
    private static final String VERIFY_CODE_PREFIX = "verify:code:";
    
    /**
     * 用户注册
     */
    @Override
    public Result<?> register(UserRegisterDTO registerDTO) {
        // 1. 验证码校验
        String email = registerDTO.getEmail();
        String phone = registerDTO.getPhone();
        
        // 验证邮箱或手机号至少有一个
        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone)) {
            return Result.error(Result.EMAIL_PHONE_REQUIRED, "邮箱和手机号至少提供一个");
        }
        
        // 验证验证码
        if (!verifyCode(email, registerDTO.getVerifyCode())) {
            return Result.error(Result.VERIFY_CODE_ERROR, "验证码错误或已过期");
        }
        
        // 2. 检查用户名是否已存在
        if (userMapper.existsByUsername(registerDTO.getUsername())) {
            return Result.error(Result.USERNAME_EXISTS, "用户名已存在");
        }
        
        // 3. 检查邮箱是否已存在
        if (StringUtils.hasText(email) && userMapper.existsByEmail(email)) {
            return Result.error(Result.EMAIL_EXISTS, "邮箱已被注册");
        }
        
        // 4. 检查手机号是否已存在
        if (StringUtils.hasText(phone) && userMapper.existsByPhone(phone)) {
            return Result.error(Result.PHONE_EXISTS, "手机号已被注册");
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
        user.setAddress(registerDTO.getAddress());
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
        
        // 10. 返回用户信息
        return Result.success(createdUser);
    }
    
    /**
     * 用户登录
     */
    @Override
    public Result<UserLoginResponseDTO> login(UserLoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "用户名或密码错误");
        }
        
        // 2. 密码校验 - 修改为使用BCrypt
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error(Result.UNAUTHORIZED, "用户名或密码错误");
        }
        
        // 3. 生成JWT令牌
        Map<String, String> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getUserId().toString());
        String token = jwtUtil.generateToken(claims);
        
        // 4. 创建用户返回信息（移除敏感数据）
        User userInfo = new User();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setGender(user.getGender());
        userInfo.setAddress(user.getAddress());
        userInfo.setAge(user.getAge());
        userInfo.setIsVip(user.getIsVip());
        userInfo.setRole(user.getRole());
        
        // 5. 创建登录响应DTO
        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO(token, userInfo);
        
        // 6. 返回登录成功结果
        return Result.success("登录成功", responseDTO);
    }
    
    /**
     * 获取用户信息
     */
    @Override
    public Result<?> getUserInfo(Long userId) {
        // 根据ID查询用户
        User user = userMapper.findById(userId);
        
        if (user == null) {
            return Result.error(Result.NOT_FOUND, "用户不存在");
        }
        
        // 清除敏感信息
        user.setPassword(null);
        
        return Result.success(user);
    }
    
    /**
     * 验证码校验
     */
    @Override
    public boolean verifyCode(String email, String code) {
        // 实际项目中应从Redis中获取验证码进行校验
        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            return false;
        }
        
        String key = VERIFY_CODE_PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(key);
        
        // 为了方便测试，可以设置一个固定的验证码供测试使用
        if ("123456".equals(code)) {
            return true;
        }
        
        // 校验验证码
        boolean match = code.equals(savedCode);
        if (match) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
        }
        return match;
    }
    
    /**
     * 更新用户信息
     */
    @Override
    public Result<User> updateUserInfo(UserUpdateDTO updateDTO) {
        // 1. 验证用户是否存在
        Long userId = updateDTO.getUserId();
        if (userId == null) {
            return Result.error(Result.BAD_REQUEST, "用户ID不能为空");
        }
        
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error(Result.NOT_FOUND, "用户不存在");
        }
        
        // 2. 验证用户名是否已被其他用户使用
        String newUsername = updateDTO.getUsername();
        if (StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())) {
            if (userMapper.existsByUsername(newUsername)) {
                return Result.error(Result.USERNAME_EXISTS, "用户名已存在");
            }
            user.setUsername(newUsername);
        }
        
        // 3. 验证邮箱是否已被其他用户使用
        String newEmail = updateDTO.getEmail();
        if (StringUtils.hasText(newEmail) && !newEmail.equals(user.getEmail())) {
            if (userMapper.existsByEmail(newEmail)) {
                return Result.error(Result.EMAIL_EXISTS, "邮箱已被注册");
            }
            user.setEmail(newEmail);
        }
        
        // 4. 验证手机号是否已被其他用户使用
        String newPhone = updateDTO.getPhone();
        if (StringUtils.hasText(newPhone) && !newPhone.equals(user.getPhone())) {
            if (userMapper.existsByPhone(newPhone)) {
                return Result.error(Result.PHONE_EXISTS, "手机号已被注册");
            }
            user.setPhone(newPhone);
        }
        
        // 5. 更新其他信息
        if (StringUtils.hasText(updateDTO.getGender())) {
            user.setGender(updateDTO.getGender());
        }
        
        if (StringUtils.hasText(updateDTO.getAddress())) {
            user.setAddress(updateDTO.getAddress());
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
