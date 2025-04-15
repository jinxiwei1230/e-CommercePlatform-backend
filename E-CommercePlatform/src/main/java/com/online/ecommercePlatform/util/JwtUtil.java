package com.online.ecommercePlatform.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT令牌
 */
@Component
public class JwtUtil {

    // JWT密钥，实际应配置在application.yml中
    @Value("${jwt.secret:defaultSecretKey}")
    private String secret;

    // JWT过期时间（小时），默认24小时
    @Value("${jwt.expiration:24}")
    private int expiration;

    /**
     * 生成JWT令牌
     * @param payload 载荷信息（claim键值对）
     * @return JWT令牌
     */
    public String generateToken(Map<String, String> payload) {
        // 创建JWT Builder
        JWTCreator.Builder builder = JWT.create();
        
        // 添加payload
        payload.forEach(builder::withClaim);
        
        // 设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expiration);
        
        // 生成令牌
        return builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(secret));
    }
    
    /**
     * 验证并解析JWT令牌
     * @param token JWT令牌
     * @return 解析后的JWT对象，如果验证失败返回null
     */
    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // 验证失败时记录错误并返回null
            System.err.println("JWT验证失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 判断令牌是否过期或无效
     * @param token JWT令牌
     * @return 是否过期或无效
     */
    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            if (jwt == null) {
                System.err.println("令牌验证失败，无法解析");
                return true;  // 验证失败视为过期
            }
            
            Date expiresAt = jwt.getExpiresAt();
            boolean expired = expiresAt == null || expiresAt.before(new Date());
            
            if (expired) {
                System.err.println("令牌已过期: " + expiresAt);
            }
            
            return expired;
        } catch (Exception e) {
            System.err.println("检查令牌过期时发生异常: " + e.getMessage());
            e.printStackTrace();
            return true;  // 发生异常视为过期
        }
    }
    
    /**
     * 从令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名，如果验证失败返回null
     */
    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim("username").asString();
    }
} 