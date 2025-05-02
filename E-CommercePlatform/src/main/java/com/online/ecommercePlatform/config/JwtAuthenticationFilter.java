package com.online.ecommercePlatform.config;

import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 处理基于JWT的身份验证
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // 添加日志记录器

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("JwtAuthenticationFilter: Received request - Method: {}, Path: {}", request.getMethod(), request.getRequestURI()); // 记录进入请求

        // 获取请求的路径
        String requestPath = request.getRequestURI();
        
        // 放行OPTIONS请求（预检请求）
        if (request.getMethod().equals("OPTIONS")) {
            log.debug("JwtAuthenticationFilter: OPTIONS request detected, passing through.");
            filterChain.doFilter(request, response);
            return;
        }
        
        // 放行注册和登录接口
        if (requestPath.equals("/api/user/register") || requestPath.equals("/api/user/login")) {
            log.debug("JwtAuthenticationFilter: Public path detected ({}), passing through.", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        // 从请求头中获取token
        String authHeader = request.getHeader("Authorization");
        
        // 如果没有Authorization头或不是Bearer token，直接放行 (对于需要认证的接口，后续会被Security拒绝)
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.debug("JwtAuthenticationFilter: No valid Bearer token found, passing through.");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 提取Token（去掉"Bearer "前缀）
            String token = authHeader.substring(7);
            log.debug("JwtAuthenticationFilter: Attempting to verify token.");
            
            // 1. 验证token有效性
            com.auth0.jwt.interfaces.DecodedJWT jwt = jwtUtil.verifyToken(token);
            
            // 2. 如果token有效且未过期
            if (jwt != null && !jwtUtil.isTokenExpired(token)) {
                // 从Token中获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                
                if (StringUtils.hasText(username)) {
                    log.info("JwtAuthenticationFilter: Authenticated user: {}", username);
                    
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                    
                    // 设置认证上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JwtAuthenticationFilter: Security context updated for user: {}", username);
                }
            } else {
                log.warn("JwtAuthenticationFilter: Invalid or expired JWT token detected.");
            }
        } catch (Exception e) {
            log.error("JwtAuthenticationFilter: Error during JWT authentication", e);
        }
        
        // 继续过滤器链
        log.debug("JwtAuthenticationFilter: Continuing filter chain.");
        filterChain.doFilter(request, response);
    }
} 