package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
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
            return Result.error("邮箱和手机号至少提供一个");
        }
        
        // 验证验证码
        if (!verifyCode(email, registerDTO.getVerifyCode())) {
            return Result.error("验证码错误或已过期");
        }
        
        // 2. 检查用户名是否已存在
        if (userMapper.existsByUsername(registerDTO.getUsername())) {
            return Result.error("用户名已存在");
        }
        
        // 3. 检查邮箱是否已存在
        if (StringUtils.hasText(email) && userMapper.existsByEmail(email)) {
            return Result.error("邮箱已被注册");
        }
        
        // 4. 检查手机号是否已存在
        if (StringUtils.hasText(phone) && userMapper.existsByPhone(phone)) {
            return Result.error("手机号已被注册");
        }
        
        // 5. 密码加密 - 修改为使用BCrypt
        String encryptedPassword = passwordEncoder.encode(registerDTO.getPassword());
        
        // 6. 创建用户对象
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setPhone(phone);
        user.setIsVip(false);
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 7. 插入用户数据
        userMapper.insert(user);
        
        // 8. 返回结果
        return Result.success("注册成功", null);
    }
    
    /**
     * 用户登录
     */
    @Override
    public Result<String> login(UserLoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 2. 密码校验 - 修改为使用BCrypt
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        // 3. 生成JWT令牌
        Map<String, String> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getUserId().toString());
        String token = jwtUtil.generateToken(claims);
        
        // 4. 返回登录成功结果
        return Result.success("登录成功", token);
    }
    
    /**
     * 获取用户信息
     */
    @Override
    public Result<?> getUserInfo(Long userId) {
        // 根据ID查询用户
        User user = userMapper.findById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
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
}
