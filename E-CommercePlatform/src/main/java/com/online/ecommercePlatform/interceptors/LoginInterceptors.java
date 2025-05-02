package com.online.ecommercePlatform.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.ecommercePlatform.dto.Result;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptors.class); // 添加日志记录器

    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("LoginInterceptors: Intercepting request - Method: {}, Path: {}", request.getMethod(), request.getRequestURI()); // 记录进入请求
        
        // 放行OPTIONS请求（预检请求）
        if (request.getMethod().equals("OPTIONS")) {
            log.debug("LoginInterceptors: OPTIONS request detected, allowing.");
            return true; // 对于拦截器，返回true表示放行
        }
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        // 验证token是否存在
        if (!StringUtils.hasText(token)) {
            log.warn("LoginInterceptors: No Authorization token found for path: {}", request.getRequestURI());
            handleUnauthorized(response, "未登录");
            return false;
        }
        
        // 去掉Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            log.debug("LoginInterceptors: Verifying token for path: {}", request.getRequestURI());
            // 验证token是否有效
            if (jwtUtil.isTokenExpired(token)) {
                log.warn("LoginInterceptors: Expired token detected for path: {}", request.getRequestURI());
                handleUnauthorized(response, "登录已过期，请重新登录");
                return false;
            }
            
            // 从token中获取用户名，并保存到请求属性中，方便后续使用
            String username = jwtUtil.getUsernameFromToken(token);
            request.setAttribute("username", username);
            log.debug("LoginInterceptors: Token verified for user: {}, allowing request for path: {}", username, request.getRequestURI());
            return true; // 验证通过，放行
        } catch (Exception e) {
            log.error("LoginInterceptors: Invalid token detected for path: {}", request.getRequestURI(), e);
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
        log.debug("LoginInterceptors: Responded with 401 Unauthorized - Message: {}", message);
    }
}