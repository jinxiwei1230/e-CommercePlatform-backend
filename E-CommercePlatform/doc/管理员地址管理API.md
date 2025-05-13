# 管理员地址管理 API

本文档描述了电子商务平台管理员对用户地址进行管理的API接口。

## 接口列表

| 接口名称 | 接口路径 | 请求方式 | 描述 |
|---------|--------|---------|------|
| 查询用户地址列表 | /api/address/admin/{userId} | GET | 管理员查询指定用户的地址列表 |

---

## 查询用户地址列表

### 接口描述

管理员查询指定用户的地址列表，返回分页结果。该接口需要管理员权限。

### 请求URL

```
GET /api/address/admin/{userId}
```

### 请求参数

#### 路径参数

| 参数名 | 类型 | 是否必须 | 描述 |
|-------|------|--------|------|
| userId | Long | 是 | 要查询的用户ID |

#### 查询参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
|-------|------|--------|------|-------|
| page | Integer | 否 | 页码，从1开始 | 1 |
| pageSize | Integer | 否 | 每页记录数 | 10 |

### 请求头

| 参数名 | 类型 | 是否必须 | 描述 |
|-------|------|--------|------|
| Authorization | String | 是 | Bearer + 空格 + token，用于验证管理员身份 |

### 请求示例

```
GET /api/address/admin/123?page=1&pageSize=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 响应参数

| 参数名 | 类型 | 描述 |
|-------|------|------|
| code | Integer | 状态码，200表示成功 |
| message | String | 返回消息 |
| data | Object | 返回数据 |
| data.total_count | Long | 总记录数 |
| data.items | Array | 地址列表 |
| data.total_pages | Integer | 总页数 |
| data.current_page | Integer | 当前页码 |

#### 地址项结构

| 参数名 | 类型 | 描述 |
|-------|------|------|
| address_id | Long | 地址ID |
| user_id | Long | 用户ID |
| recipient_name | String | 收件人姓名 |
| phone | String | 联系电话 |
| address_detail | String | 详细地址 |
| city | String | 城市 |
| state | String | 省份/州 |
| postal_code | String | 邮政编码 |
| is_default | Boolean | 是否为默认地址 |
| create_time | String | 创建时间，格式：yyyy-MM-dd HH:mm:ss |
| update_time | String | 更新时间，格式：yyyy-MM-dd HH:mm:ss |

### 响应示例

```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "total_count": 2,
        "items": [
            {
                "address_id": 1,
                "user_id": 123,
                "recipient_name": "张三",
                "phone": "13800138000",
                "address_detail": "中关村科技园区23号楼",
                "city": "北京市",
                "state": "北京",
                "postal_code": "100000",
                "is_default": true,
                "create_time": "2023-10-01 12:00:00",
                "update_time": "2023-10-01 12:00:00"
            },
            {
                "address_id": 2,
                "user_id": 123,
                "recipient_name": "李四",
                "phone": "13900139000",
                "address_detail": "望京科技产业园45号",
                "city": "北京市",
                "state": "北京",
                "postal_code": "100102",
                "is_default": false,
                "create_time": "2023-11-15 09:30:00",
                "update_time": "2023-11-15 09:30:00"
            }
        ],
        "total_pages": 1,
        "current_page": 1
    }
}
```

### 错误码

| 错误码 | 描述 | 原因 |
|-------|------|------|
| 400 | 参数错误 | 请求参数不符合要求 |
| 401 | 未授权 | 未提供token或token无效/过期 |
| 403 | 权限不足 | 当前用户不是管理员 |
| 404 | 用户不存在 | 指定的用户ID不存在 |
| 500 | 服务器错误 | 服务器内部错误 | 