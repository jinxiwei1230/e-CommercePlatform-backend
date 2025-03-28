package com.online.ecommercePlatform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析JSON Web Token
 */
public class JwtUtil {

    // JWT加密密钥（实际项目中应该从配置中读取）
    private static final String KEY = "chj";

    // JSON序列化/反序列化工具
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 生成JWT Token
     * @param claims 要存储在Token中的业务数据（键值对）
     * @return 生成的JWT Token字符串
     * @throws JsonProcessingException 如果JSON序列化失败
     */
    public static String genToken(Map<String, Object> claims) throws JsonProcessingException {
        // 将业务数据Map转换为JSON字符串
        String claimsJson = objectMapper.writeValueAsString(claims);

        // 使用JWT库创建Token
        return JWT.create()
                // 设置自定义声明（payload）
                .withClaim("claims", claimsJson)
                // 设置过期时间（当前时间+12小时）
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                // 使用HS256算法和密钥签名
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * 解析JWT Token并返回业务数据
     * @param token 要解析的JWT Token字符串
     * @return 包含业务数据的Map
     * @throws JsonProcessingException 如果JSON反序列化失败
     * @throws com.auth0.jwt.exceptions.JWTVerificationException 如果Token验证失败
     */
    public static Map<String, Object> parseToken(String token) throws JsonProcessingException {
        // 验证Token并提取claims JSON字符串
        String claimsJson = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asString();

        // 将JSON字符串反序列化为Map对象
        return objectMapper.readValue(claimsJson, new TypeReference<Map<String, Object>>() {});
    }
}