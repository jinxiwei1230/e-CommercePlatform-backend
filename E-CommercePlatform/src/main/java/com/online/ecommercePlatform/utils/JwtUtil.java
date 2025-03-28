package com.online.ecommercePlatform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "chj";
    private static final ObjectMapper objectMapper = new ObjectMapper(); // JSON 工具
	
	//接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) throws JsonProcessingException {
        // 将 Map 转换为 JSON 字符串
        String claimsJson = objectMapper.writeValueAsString(claims);

        return JWT.create()
                .withClaim("claims", claimsJson)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .sign(Algorithm.HMAC256(KEY));
    }

	//接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) throws JsonProcessingException {
        // 提取 claims 的 JSON 字符串
        String claimsJson = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asString();

        // 将 JSON 字符串反序列化为 Map
        return objectMapper.readValue(claimsJson, new TypeReference<Map<String, Object>>() {});


/*        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();*/
    }

}
