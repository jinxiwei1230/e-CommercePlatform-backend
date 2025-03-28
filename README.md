# e-CommercePlatform-backend

# 电商系统后端 - Spring Boot 3 实现

## 项目概述

这是一个基于Spring Boot 3构建的电商系统后端服务，提供完整的电子商务平台后端功能，包括用户管理、商品管理、订单处理、支付集成等功能。

## 技术栈

- **核心框架**: Spring Boot 3.x
- **数据库**: MySQL 8.x
- **缓存**: Redis
- **构建工具**: Maven

## 环境要求

- JDK 17+

## 数据库配置

修改应用程序配置:
在 `application.yml`  中配置数据库连接:

```yaml
spring:
  datasource:
    username: xxx
    password: xxxxx
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/database
  data:
    redis:
      port: 6379
      host: localhost

```

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── online/
│   │           └── ecommercePlatform/
│   │               ├── config/        # 配置类
│   │               ├── interceptors/  # 拦截器
│   │               ├── controller/    # 控制器
│   │               ├── exception/     # 异常处理
│   │               ├── pojo/          # 实体类
│   │               ├── mapper/        # 数据访问层
│   │               ├── service/       # 业务逻辑层
│   │               ├── util/          # 工具类
│   │               └── ECommercePlatformApplication.java # 启动类
│   └── resources/
│       ├── static/      # 静态资源
│       ├── templates/   # 模板文件
│       └── application.yml # 应用配置
└── test/                # 测试代码
```

