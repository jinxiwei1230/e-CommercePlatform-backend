package com.online.ecommercePlatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.dto.UserLoginDTO;
import com.online.ecommercePlatform.dto.UserRegisterDTO;
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


    /**
     * 更新用户信息（不包括密码）
     * @param user 更新后的用户对象
     * @return 更新结果
     */
    @Override
    @Transactional
    public User updateUser(User user) {
        // 校验输入
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("用户对象或用户ID不能为空");
        }

        // 检查用户是否存在
        User existingUser = userMapper.findById(user.getUserId());
        if (existingUser == null) {
            throw new RuntimeException("用户ID " + user.getUserId() + " 不存在");
        }

        // 验证唯一性约束（如果更新了 username、email 或 phone）
        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            if (userMapper.existsByUsername(user.getUsername())) {
                throw new RuntimeException("用户名 " + user.getUsername() + " 已存在");
            }
        }
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userMapper.existsByEmail(user.getEmail())) {
                throw new RuntimeException("邮箱 " + user.getEmail() + " 已存在");
            }
        }
        if (user.getPhone() != null && !user.getPhone().equals(existingUser.getPhone())) {
            if (userMapper.existsByPhone(user.getPhone())) {
                throw new RuntimeException("手机号 " + user.getPhone() + " 已存在");
            }
        }

        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());

        // 执行更新操作
        int rowsAffected = userMapper.update(user);
        if (rowsAffected == 0) {
            throw new RuntimeException("更新用户失败");
        }

        // 返回更新后的用户对象
        return userMapper.findById(user.getUserId());
    }

    /**
     * 更新用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新结果
     */
    @Transactional
    public User updatePassword(Long userId, String oldPassword, String newPassword) {
        // 校验输入
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("新密码不能为空");
        }

        // 检查用户是否存在
        User existingUser = userMapper.findById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户ID " + userId + " 不存在");
        }

        // 验证旧密码
        if (oldPassword == null || !passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 创建用户对象，仅更新密码和更新时间
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());

        // 执行更新操作
        int rowsAffected = userMapper.update(user);
        if (rowsAffected == 0) {
            throw new RuntimeException("更新密码失败");
        }

        // 返回更新后的用户对象
        return userMapper.findById(userId);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        // 校验输入
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 检查用户是否存在
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("用户ID " + id + " 不存在");
        }

        // 执行删除
        int rowsAffected = userMapper.deleteById(id);
        return rowsAffected > 0;
    }


    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    public int deleteUserBatch(List<Long> ids) {
        // 校验输入
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("用户ID列表不能为空");
        }

        // 检查是否存在无效ID（可选，增强健壮性）
        for (Long id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("用户ID列表中包含空值");
            }
        }

        // 执行批量删除
        int rowsAffected = userMapper.deleteBatch(ids);
        return rowsAffected;
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

}
