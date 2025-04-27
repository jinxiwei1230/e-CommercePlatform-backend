package com.online.ecommercePlatform.config;

import com.online.ecommercePlatform.interceptors.LoginInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 主要功能：
 *   注册自定义拦截器
 *   配置拦截器规则
 *   设置排除路径
 *   配置CORS跨域
 */
@Configuration // 标记为Spring配置类
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptors loginInterceptors; // 注入登录拦截器

    /**
     * 配置跨域资源共享(CORS)
     * 允许前端应用访问后端API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 应用到所有路径
                .allowedOrigins("http://localhost:5173") // 允许的前端域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true) // 允许发送凭证信息(cookies)
                .maxAge(3600); // 预检请求缓存时间(秒)
    }

    /**
     * 添加拦截器配置
     * @param registry 拦截器注册器，用于注册和配置拦截器
     *
     * @apiNote 当前配置：
     *   排除登录和注册接口（/api/user/login, /api/user/register）
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器并配置排除路径
        registry.addInterceptor(loginInterceptors)
                // 排除不需要拦截的路径
                .excludePathPatterns(
                        "/api/user/login",    // 用户登录接口
                        "/api/user/register", // 用户注册接口
                        "/doc.html",      // Swagger文档
                        "/swagger-ui.html", // Swagger UI
                        "/swagger-resources/**", // Swagger资源
                        "/webjars/**",    // Swagger Web依赖
                        "/v3/api-docs/**" // OpenAPI文档
                );

        // 可以继续添加其他拦截器配置...
        // registry.addInterceptor(otherInterceptor)...;
    }

}