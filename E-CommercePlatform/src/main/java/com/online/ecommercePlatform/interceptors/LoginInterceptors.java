package com.online.ecommercePlatform.interceptors;

import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.utils.JwtUtil;
import com.online.ecommercePlatform.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证拦截器
 * 用于验证用户请求的JWT令牌有效性，实现基于Token的认证机制
 * 工作流程：
 *   从请求头获取Authorization令牌
 *   验证Redis中是否存在对应令牌
 *   解析JWT令牌获取用户信息
 *   将用户信息存入ThreadLocal
 *   请求完成后清理ThreadLocal
 */
@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate; // Redis操作模板，用于验证Token有效性

    /**
     * 请求预处理方法 - 执行Token验证
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 目标处理器
     * @return boolean 是否放行请求
     * @throws Exception 可能抛出的异常
     *实现逻辑：
     * 1. 从Authorization头获取JWT令牌
     * 2. 检查Redis中是否存在该令牌（防止令牌被注销）
     * 3. 验证JWT令牌有效性
     * 4. 解析令牌并将声明存入ThreadLocal
     * 5. 验证失败返回401未授权状态
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取令牌
        String token = request.getHeader("Authorization");

        try {
            // 检查Redis中是否存在该令牌（实现令牌注销功能）
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get("token");

            if (redisToken == null) {
                // Redis中不存在该令牌，表示令牌已失效（用户已登出或令牌被撤销）
                throw new RuntimeException();
            }

            // 解析JWT令牌
            Map<String,Object> claims = JwtUtil.parseToken(token);
            System.out.println("解析出的Token声明: " + claims);

            // 将用户信息存入ThreadLocal，供后续业务使用
            ThreadLocalUtil.set(claims);

            return true; // 验证通过，放行请求
        } catch (Exception e) {
            // 设置401未授权状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false; // 验证失败，拦截请求
        }
    }

    /**
     * 请求完成后清理ThreadLocal，防止内存泄漏
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 目标处理器
     * @param ex 处理过程中抛出的异常（如果有）
     * @warning 必须清理ThreadLocal，特别是在线程池环境下
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}