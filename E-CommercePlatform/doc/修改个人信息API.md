### 用户管理接口文档

---

## 1. 获取当前登录用户信息

**请求方式**  
`GET /api/user/selectById/current`

**请求头**  
| 参数名称      | 参数值         | 是否必填 | 说明        |
| ------------- | -------------- | -------- | ----------- |
| Authorization | Bearer {token} | 是       | JWT认证令牌 |

**成功响应示例**  
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "userId": 7,
        "username": "admin2",
        "password": null,
        "email": "admin@example.com",
        "phone": "13888345678",
        "gender": null,
        "age": null,
        "isVip": false,
        "role": null,
        "createTime": "2025-04-16T19:40:18",
        "updateTime": "2025-04-16T19:40:18"
    }
}
```

**错误响应**  

| 状态码 | 说明                   |
| ------ | ---------------------- |
| 401    | 未提供Token或Token无效 |
| 403    | Token已过期            |

---

## 2. 更新用户基本信息

**请求方式**  
`PUT /api/user/update`

**请求头**  
| 参数名称      | 参数值           | 是否必填 | 说明        |
| ------------- | ---------------- | -------- | ----------- |
| Content-Type  | application/json | 是       |             |
| Authorization | Bearer {token}   | 是       | JWT认证令牌 |

**请求体参数**  
```json
{
    "userId": 2,
    "username": "3new",
    "email": "new@example.com",
    "phone": "1234567890",
    "gender": "女",
    "age": 30,
    "role": "普通用户"
}
```

**注意事项**  
- 密码字段会被自动忽略（即使传入也不生效）
- 至少需要传递一个可修改字段

**成功响应示例**  
```json
{
    "code": 200,
    "message": "用户信息更新成功",
    "data": {
        "userId": 2,
        "username": "3new",
        "password": "password2",
        "email": "new@example.com",
        "phone": "1234567890",
        "gender": "女",
        "age": 30,
        "isVip": false,
        "role": "普通用户",
        "createTime": "2025-03-31T20:27:50",
        "updateTime": "2025-04-16T19:58:44"
    }
}
```

---

## 3. 更新用户密码

**请求方式**  
`PUT /api/user/updatePassword`

**请求头**  
| 参数名称      | 参数值           | 是否必填 | 说明        |
| ------------- | ---------------- | -------- | ----------- |
| Content-Type  | application/json | 是       |             |
| Authorization | Bearer {token}   | 是       | JWT认证令牌 |

**请求体参数**  
```json
{
    "oldPassword": "原密码",
    "newPassword": "新密码"
}
```

**密码要求**  
- 新密码长度至少6位
- 新旧密码不能相同

**成功响应示例**  
```json
{
    "code": 200,
    "message": "密码更新成功",
    "data": {
        "userId": 7,
        "username": "admin2",
        "password": "$2a$10$hlhFmX2jzV4ZaczwZwt20...",
        "email": "admin@example.com",
        "phone": "13888345678",
        "gender": null,
        "age": null,
        "isVip": false,
        "role": null,
        "createTime": "2025-04-16T19:40:18",
        "updateTime": "2025-04-16T22:19:42"
    }
}
```

**错误响应**  
| 状态码 | 说明             |
| ------ | ---------------- |
| 401    | Token无效或过期  |
| 422    | 新密码不符合要求 |

---

## 通用响应结构

```json
{
    "code": 数字状态码,
    "message": "操作结果描述",
    "data": { 实际数据对象或null }
}
```
