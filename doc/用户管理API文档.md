# 用户管理接口文档

本文档描述了电子商务平台用户管理相关的API接口。

## 基础信息

- 基础路径: `/user`
- 返回格式: 所有接口均返回统一的JSON格式，包含状态码、消息和数据
- 认证方式: 部分接口需要身份验证（登录后获取的token）

## 接口列表

### 1. 用户注册

- **URL**: `/user/register`
- **方法**: POST
- **描述**: 新用户注册
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | username | String | 是 | 用户名 |
  | password | String | 是 | 密码 |
  | email | String | 否 | 电子邮箱 |
  | phone | String | 否 | 手机号码 |
  | gender | String | 否 | 性别 |
  | address | String | 否 | 地址 |

- **请求示例**:

  ```json
  {
    "username": "zhangsan",
    "password": "123456",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "gender": "男",
    "address": "北京市朝阳区"
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 注册成功的用户信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "userId": 1001,
      "username": "zhangsan",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "gender": "男",
      "address": "北京市朝阳区"
    }
  }
  ```

### 2. 用户登录

- **URL**: `/user/login`
- **方法**: POST
- **描述**: 用户登录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | username | String | 是 | 用户名 |
  | password | String | 是 | 密码 |

- **请求示例**:

  ```
  /user/login?username=zhangsan&password=123456
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 登录成功的用户信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "userId": 1001,
      "username": "zhangsan",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "gender": "男",
      "address": "北京市朝阳区"
    }
  }
  ```

### 3. 新增用户

- **URL**: `/user/add`
- **方法**: POST
- **描述**: 管理员添加新用户
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | username | String | 是 | 用户名 |
  | password | String | 是 | 密码 |
  | email | String | 否 | 电子邮箱 |
  | phone | String | 否 | 手机号码 |
  | gender | String | 否 | 性别 |
  | address | String | 否 | 地址 |

- **请求示例**:

  ```json
  {
    "username": "zhangsan",
    "password": "123456",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "gender": "男",
    "address": "北京市朝阳区"
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 添加成功的用户信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "userId": 1001,
      "username": "zhangsan",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "gender": "男",
      "address": "北京市朝阳区"
    }
  }
  ```

### 4. 获取用户信息

- **URL**: `/user/selectById/{id}`
- **方法**: GET
- **描述**: 根据用户ID获取用户信息
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 用户ID，路径参数 |

- **请求示例**:

  ```
  GET /user/selectById/1001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 用户信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "userId": 1001,
      "username": "zhangsan",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "gender": "男",
      "address": "北京市朝阳区"
    }
  }
  ```

### 5. 更新用户信息

- **URL**: `/user/update`
- **方法**: PUT
- **描述**: 更新用户信息
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | userId | Long | 是 | 用户ID |
  | username | String | 否 | 用户名 |
  | email | String | 否 | 电子邮箱 |
  | phone | String | 否 | 手机号码 |
  | gender | String | 否 | 性别 |
  | address | String | 否 | 地址 |

- **请求示例**:

  ```json
  {
    "userId": 1001,
    "username": "zhangsan_new",
    "email": "zhangsan_new@example.com",
    "phone": "13900139000",
    "gender": "男",
    "address": "上海市浦东新区"
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 更新后的用户信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "userId": 1001,
      "username": "zhangsan_new",
      "email": "zhangsan_new@example.com",
      "phone": "13900139000",
      "gender": "男",
      "address": "上海市浦东新区"
    }
  }
  ```

### 6. 删除用户

- **URL**: `/user/delete/{id}`
- **方法**: DELETE
- **描述**: 根据ID删除用户
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 用户ID，路径参数 |

- **请求示例**:

  ```
  DELETE /user/delete/1001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Boolean | 删除结果 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": true
  }
  ```

### 7. 批量删除用户

- **URL**: `/user/delete/batch`
- **方法**: DELETE
- **描述**: 批量删除多个用户
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | ids | Array | 是 | 用户ID列表 |

- **请求示例**:

  ```json
  [1001, 1002, 1003]
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Integer | 成功删除的用户数量 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": 3
  }
  ```

### 8. 获取所有用户

- **URL**: `/user/selectAll`
- **方法**: GET
- **描述**: 获取所有用户列表
- **请求参数**: 

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | username | String | 否 | 用户名（模糊查询） |
  | gender | String | 否 | 性别 |
  | email | String | 否 | 电子邮箱（模糊查询） |

- **请求示例**:

  ```
  GET /user/selectAll?username=zhang&gender=男
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Array | 用户列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": [
      {
        "userId": 1001,
        "username": "zhangsan",
        "email": "zhangsan@example.com",
        "phone": "13800138000",
        "gender": "男",
        "address": "北京市朝阳区"
      },
      {
        "userId": 1002,
        "username": "lisi",
        "email": "lisi@example.com",
        "phone": "13800138001",
        "gender": "女",
        "address": "广州市天河区"
      }
    ]
  }
  ```

### 9. 分页查询用户

- **URL**: `/user/selectPage`
- **方法**: GET
- **描述**: 分页查询用户信息，支持条件筛选
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | pageNum | Integer | 否 | 页码，默认为1 |
  | pageSize | Integer | 否 | 每页条数，默认为10 |
  | username | String | 否 | 用户名（模糊查询） |
  | gender | String | 否 | 性别 |
  | email | String | 否 | 电子邮箱（模糊查询） |

- **请求示例**:

  ```
  GET /user/selectPage?pageNum=1&pageSize=10&username=zhang&gender=男
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 分页结果 |
  | data.total | Long | 总记录数 |
  | data.items | Array | 用户列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "total": 25,
      "items": [
        {
          "userId": 1001,
          "username": "zhangsan",
          "email": "zhangsan@example.com",
          "phone": "13800138000",
          "gender": "男",
          "address": "北京市朝阳区"
        },
        {
          "userId": 1004,
          "username": "zhangwei",
          "email": "zhangwei@example.com",
          "phone": "13800138004",
          "gender": "男",
          "address": "上海市静安区"
        }
      ]
    }
  }
  ```

## 错误码说明

| 错误码 | 描述 |
| ------ | ------ |
| 0 | 操作失败 |
| 1 | 操作成功 |

## 注意事项

1. 所有接口在请求失败时都会返回相应的错误信息
2. 用户密码在传输和存储时应当进行加密处理
3. 部分接口可能需要身份验证，请在请求头中携带token
4. `/user/register`接口主要用于用户自助注册，`/user/add`接口主要用于管理员添加用户 