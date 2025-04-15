package com.online.ecommercePlatform.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 用于验证请求是否携带有效的JWT令牌
 */
@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求（预检请求）
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        // 验证token是否存在
        if (!StringUtils.hasText(token)) {
            handleUnauthorized(response, "未登录");
            return false;
        }
        
        // 去掉Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证token是否有效
            if (jwtUtil.isTokenExpired(token)) {
                handleUnauthorized(response, "登录已过期，请重新登录");
                return false;
            }
            
            // 从token中获取用户名，并保存到请求属性中，方便后续使用
            String username = jwtUtil.getUsernameFromToken(token);
            request.setAttribute("username", username);
            return true;
        } catch (Exception e) {
            handleUnauthorized(response, "无效的令牌");
            return false;
        }
    }
    
    /**
     * 处理未授权的响应
     */
    private void handleUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        
        // 使用Result对象返回错误信息
        Result<?> result = Result.error(message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}