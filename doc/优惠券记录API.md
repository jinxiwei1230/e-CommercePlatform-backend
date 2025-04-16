# 优惠券发放记录接口文档

本文档描述了电子商务平台优惠券发放记录相关的API接口。

## 基础信息

- 基础路径: `/coupon/distribution`
- 返回格式: 所有接口均返回统一的JSON格式，包含状态码、消息和数据
- 认证方式: 管理员接口需要管理员权限认证

## 接口列表

### 1. 创建优惠券发放记录

- **URL**: `/coupon/distribution/add`
- **方法**: POST
- **描述**: 创建新的优惠券发放记录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | couponId | Long | 是 | 优惠券ID |
  | userId | Long | 是 | 用户ID |
  | status | String | 否 | 使用状态(未使用/已使用/已过期)，默认未使用 |
  | useTime | Date | 否 | 使用时间 |
  | orderId | Long | 否 | 关联订单ID |

- **请求示例**:

  ```json
  {
    "couponId": 1001,
    "userId": 2001,
    "status": "未使用"
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 创建成功的优惠券发放记录信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "distributionId": 3001,
      "couponId": 1001,
      "userId": 2001,
      "status": "未使用",
      "useTime": null,
      "orderId": null,
      "createTime": "2023-06-01 10:00:00",
      "updateTime": "2023-06-01 10:00:00"
    }
  }
  ```

### 2. 批量发放优惠券

- **URL**: `/coupon/distribution/batchAdd`
- **方法**: POST
- **描述**: 批量将优惠券发放给多个用户
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | couponId | Long | 是 | 优惠券ID |
  | userIds | Array | 是 | 用户ID列表 |
  | genderFilter | String | 否 | 性别筛选条件 |
  | regionFilter | String | 否 | 地区筛选条件 |

- **请求示例**:

  ```json
  {
    "couponId": 1001,
    "userIds": [2001, 2002, 2003],
    "genderFilter": "男",
    "regionFilter": "北京市"
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Integer | 成功发放的记录数量 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": 3
  }
  ```

### 3. 更新优惠券发放记录

- **URL**: `/coupon/distribution/update`
- **方法**: PUT
- **描述**: 更新优惠券发放记录信息
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | distributionId | Long | 是 | 优惠券发放记录ID |
  | status | String | 否 | 使用状态 |
  | useTime | Date | 否 | 使用时间 |
  | orderId | Long | 否 | 关联订单ID |

- **请求示例**:

  ```json
  {
    "distributionId": 3001,
    "status": "已使用",
    "useTime": "2023-06-05 15:30:00",
    "orderId": 5001
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 更新后的优惠券发放记录信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "distributionId": 3001,
      "couponId": 1001,
      "userId": 2001,
      "status": "已使用",
      "useTime": "2023-06-05 15:30:00",
      "orderId": 5001,
      "createTime": "2023-06-01 10:00:00",
      "updateTime": "2023-06-05 15:30:00"
    }
  }
  ```

### 4. 根据ID查询优惠券发放记录

- **URL**: `/coupon/distribution/selectById/{id}`
- **方法**: GET
- **描述**: 根据ID查询优惠券发放记录详情
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 优惠券发放记录ID，路径参数 |

- **请求示例**:

  ```
  GET /coupon/distribution/selectById/3001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 优惠券发放记录信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "distributionId": 3001,
      "couponId": 1001,
      "userId": 2001,
      "status": "已使用",
      "useTime": "2023-06-05 15:30:00",
      "orderId": 5001,
      "createTime": "2023-06-01 10:00:00",
      "updateTime": "2023-06-05 15:30:00",
      "coupon": {
        "couponId": 1001,
        "type": "满减",
        "name": "618满减券",
        "description": "618活动专享满200减50",
        "discount_value": 50.00,
        "min_order_amount": 200.00
      },
      "user": {
        "userId": 2001,
        "username": "zhangsan",
        "email": "zhangsan@example.com"
      }
    }
  }
  ```

### 5. 根据用户ID查询优惠券发放记录

- **URL**: `/coupon/distribution/selectByUserId/{userId}`
- **方法**: GET
- **描述**: 根据用户ID查询该用户的优惠券发放记录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | userId | Long | 是 | 用户ID，路径参数 |

- **请求示例**:

  ```
  GET /coupon/distribution/selectByUserId/2001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Array | 优惠券发放记录列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": [
      {
        "distributionId": 3001,
        "couponId": 1001,
        "userId": 2001,
        "status": "已使用",
        "useTime": "2023-06-05 15:30:00",
        "orderId": 5001,
        "coupon": {
          "couponId": 1001,
          "type": "满减",
          "name": "618满减券",
          "description": "618活动专享满200减50"
        }
      },
      {
        "distributionId": 3005,
        "couponId": 1002,
        "userId": 2001,
        "status": "未使用",
        "useTime": null,
        "orderId": null,
        "coupon": {
          "couponId": 1002,
          "type": "折扣",
          "name": "夏季折扣券",
          "description": "夏季服装8折"
        }
      }
    ]
  }
  ```

### 6. 根据优惠券ID查询发放记录

- **URL**: `/coupon/distribution/selectByCouponId/{couponId}`
- **方法**: GET
- **描述**: 根据优惠券ID查询该优惠券的发放记录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | couponId | Long | 是 | 优惠券ID，路径参数 |

- **请求示例**:

  ```
  GET /coupon/distribution/selectByCouponId/1001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Array | 优惠券发放记录列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": [
      {
        "distributionId": 3001,
        "couponId": 1001,
        "userId": 2001,
        "status": "已使用",
        "useTime": "2023-06-05 15:30:00",
        "orderId": 5001,
        "user": {
          "userId": 2001,
          "username": "zhangsan",
          "email": "zhangsan@example.com"
        }
      },
      {
        "distributionId": 3002,
        "couponId": 1001,
        "userId": 2002,
        "status": "未使用",
        "useTime": null,
        "orderId": null,
        "user": {
          "userId": 2002,
          "username": "lisi",
          "email": "lisi@example.com"
        }
      }
    ]
  }
  ```

### 7. 删除优惠券发放记录

- **URL**: `/coupon/distribution/delete/{id}`
- **方法**: DELETE
- **描述**: 删除指定ID的优惠券发放记录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 优惠券发放记录ID，路径参数 |

- **请求示例**:

  ```
  DELETE /coupon/distribution/delete/3001
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

### 8. 批量删除优惠券发放记录

- **URL**: `/coupon/distribution/delete/batch`
- **方法**: DELETE
- **描述**: 批量删除多个优惠券发放记录
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | ids | Array | 是 | 优惠券发放记录ID列表 |

- **请求示例**:

  ```json
  [3001, 3002, 3003]
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Integer | 成功删除的记录数量 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": 3
  }
  ```

### 9. 查询所有优惠券发放记录

- **URL**: `/coupon/distribution/selectAll`
- **方法**: GET
- **描述**: 获取所有优惠券发放记录列表
- **请求参数**: 

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | couponId | Long | 否 | 优惠券ID |
  | userId | Long | 否 | 用户ID |
  | status | String | 否 | 使用状态 |

- **请求示例**:

  ```
  GET /coupon/distribution/selectAll?status=未使用
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Array | 优惠券发放记录列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": [
      {
        "distributionId": 3001,
        "couponId": 1001,
        "userId": 2001,
        "status": "已使用",
        "useTime": "2023-06-05 15:30:00",
        "orderId": 5001
      },
      {
        "distributionId": 3002,
        "couponId": 1001,
        "userId": 2002,
        "status": "未使用",
        "useTime": null,
        "orderId": null
      }
    ]
  }
  ```

### 10. 分页查询优惠券发放记录

- **URL**: `/coupon/distribution/selectPage`
- **方法**: GET
- **描述**: 分页查询优惠券发放记录，支持条件筛选
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | pageNum | Integer | 否 | 页码，默认为1 |
  | pageSize | Integer | 否 | 每页条数，默认为10 |
  | couponId | Long | 否 | 优惠券ID |
  | userId | Long | 否 | 用户ID |
  | status | String | 否 | 使用状态 |

- **请求示例**:

  ```
  GET /coupon/distribution/selectPage?pageNum=1&pageSize=10&status=未使用
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 分页结果 |
  | data.total | Long | 总记录数 |
  | data.items | Array | 优惠券发放记录列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "total": 35,
      "items": [
        {
          "distributionId": 3002,
          "couponId": 1001,
          "userId": 2002,
          "status": "未使用",
          "useTime": null,
          "orderId": null,
          "coupon": {
            "couponId": 1001,
            "type": "满减",
            "name": "618满减券"
          },
          "user": {
            "userId": 2002,
            "username": "lisi"
          }
        },
        {
          "distributionId": 3005,
          "couponId": 1002,
          "userId": 2001,
          "status": "未使用",
          "useTime": null,
          "orderId": null,
          "coupon": {
            "couponId": 1002,
            "type": "折扣",
            "name": "夏季折扣券"
          },
          "user": {
            "userId": 2001,
            "username": "zhangsan"
          }
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
2. 优惠券发放操作会检查优惠券的有效性和剩余数量
3. 优惠券使用状态包括：未使用、已使用、已过期
4. 管理员相关接口需要管理员权限 