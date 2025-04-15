package com.online.ecommercePlatform.config;

import com.online.ecommercePlatform.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 从请求头中获取token
        String authHeader = request.getHeader("Authorization");
        
        // 如果没有Authorization头或不是Bearer token，直接放行
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 提取Token（去掉"Bearer "前缀）
            String token = authHeader.substring(7);
            
            // 1. 验证token有效性
            com.auth0.jwt.interfaces.DecodedJWT jwt = jwtUtil.verifyToken(token);
            
            // 2. 如果token有效且未过期
            if (jwt != null && !jwtUtil.isTokenExpired(token)) {
                // 从Token中获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                
                if (StringUtils.hasText(username)) {
                    logger.info("认证用户: " + username);
                    
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                    
                    // 设置认证上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                logger.warn("无效的JWT令牌");
            }
        } catch (Exception e) {
            logger.error("JWT认证过程中发生异常", e);
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
} 