# 优惠券管理接口文档

本文档描述了电子商务平台优惠券管理相关的API接口。

## 基础信息

- 基础路径: `/coupon`
- 返回格式: 所有接口均返回统一的JSON格式，包含状态码、消息和数据
- 认证方式: 管理员接口需要管理员权限认证

## 接口列表

### 1. 创建优惠券

- **URL**: `/coupon/add`
- **方法**: POST
- **描述**: 创建新的优惠券
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | type | String | 是 | 优惠券类型（满减/折扣/固定金额） |
  | name | String | 是 | 优惠券名称 |
  | description | String | 否 | 优惠券描述 |
  | discount_value | Double | 是 | 折扣值 |
  | min_order_amount | Double | 是 | 最低订单金额 |
  | start_time | Date | 是 | 生效时间 |
  | end_time | Date | 是 | 失效时间 |
  | is_valid | Boolean | 否 | 是否有效，默认为true |
  | total_count | Integer | 否 | 总数量，-1表示无限制 |
  | remaining_count | Integer | 否 | 剩余数量 |

- **请求示例**:

  ```json
  {
    "type": "满减",
    "name": "新用户满减券",
    "description": "新用户专享满200减50",
    "discount_value": 50.00,
    "min_order_amount": 200.00,
    "start_time": "2023-06-01 00:00:00",
    "end_time": "2023-06-30 23:59:59",
    "is_valid": true,
    "total_count": 1000,
    "remaining_count": 1000
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 创建成功的优惠券信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "couponId": 1001,
      "type": "满减",
      "name": "新用户满减券",
      "description": "新用户专享满200减50",
      "discount_value": 50.00,
      "min_order_amount": 200.00,
      "start_time": "2023-06-01 00:00:00",
      "end_time": "2023-06-30 23:59:59",
      "is_valid": true,
      "total_count": 1000,
      "remaining_count": 1000,
      "create_time": "2023-05-25 10:00:00",
      "update_time": "2023-05-25 10:00:00"
    }
  }
  ```

### 2. 更新优惠券

- **URL**: `/coupon/update`
- **方法**: PUT
- **描述**: 更新指定ID的优惠券信息
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | couponId | Long | 是 | 优惠券ID |
  | type | String | 否 | 优惠券类型 |
  | name | String | 否 | 优惠券名称 |
  | description | String | 否 | 优惠券描述 |
  | discount_value | Double | 否 | 折扣值 |
  | min_order_amount | Double | 否 | 最低订单金额 |
  | start_time | Date | 否 | 生效时间 |
  | end_time | Date | 否 | 失效时间 |
  | is_valid | Boolean | 否 | 是否有效 |
  | total_count | Integer | 否 | 总数量 |
  | remaining_count | Integer | 否 | 剩余数量 |

- **请求示例**:

  ```json
  {
    "couponId": 1001,
    "name": "618满减券",
    "description": "618活动专享满200减50",
    "start_time": "2023-06-10 00:00:00",
    "end_time": "2023-06-20 23:59:59",
    "is_valid": true
  }
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，200表示成功，422表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 更新后的优惠券信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "couponId": 1001,
      "type": "满减",
      "name": "618满减券",
      "description": "618活动专享满200减50",
      "discount_value": 50.00,
      "min_order_amount": 200.00,
      "start_time": "2023-06-10 00:00:00",
      "end_time": "2023-06-20 23:59:59",
      "is_valid": true,
      "total_count": 1000,
      "remaining_count": 1000,
      "create_time": "2023-05-25 10:00:00",
      "update_time": "2023-05-25 15:30:00"
    }
  }
  ```

### 3. 根据ID查询优惠券

- **URL**: `/coupon/selectById/{id}`
- **方法**: GET
- **描述**: 根据ID查询优惠券详细信息
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 优惠券ID，路径参数 |

- **请求示例**:

  ```
  GET /coupon/selectById/1001
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 优惠券信息 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "couponId": 1001,
      "type": "满减",
      "name": "618满减券",
      "description": "618活动专享满200减50",
      "discount_value": 50.00,
      "min_order_amount": 200.00,
      "start_time": "2023-06-10 00:00:00",
      "end_time": "2023-06-20 23:59:59",
      "is_valid": true,
      "total_count": 1000,
      "remaining_count": 950,
      "create_time": "2023-05-25 10:00:00",
      "update_time": "2023-05-25 15:30:00"
    }
  }
  ```

### 4. 删除优惠券

- **URL**: `/coupon/delete/{id}`
- **方法**: DELETE
- **描述**: 删除指定ID的优惠券
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | id | Long | 是 | 优惠券ID，路径参数 |

- **请求示例**:

  ```
  DELETE /coupon/delete/1001
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

### 5. 批量删除优惠券

- **URL**: `/coupon/delete/batch`
- **方法**: DELETE
- **描述**: 批量删除多个优惠券
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | ids | Array | 是 | 优惠券ID列表 |

- **请求示例**:

  ```json
  [1001, 1002, 1003]
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Integer | 成功删除的优惠券数量 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": 3
  }
  ```

### 6. 查询所有优惠券

- **URL**: `/coupon/selectAll`
- **方法**: GET
- **描述**: 获取所有优惠券列表
- **请求参数**: 

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | type | String | 否 | 优惠券类型 |
  | name | String | 否 | 优惠券名称（模糊查询） |
  | is_valid | Boolean | 否 | 是否有效 |

- **请求示例**:

  ```
  GET /coupon/selectAll?type=满减&is_valid=true
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Array | 优惠券列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": [
      {
        "couponId": 1001,
        "type": "满减",
        "name": "618满减券",
        "description": "618活动专享满200减50",
        "discount_value": 50.00,
        "min_order_amount": 200.00,
        "start_time": "2023-06-10 00:00:00",
        "end_time": "2023-06-20 23:59:59",
        "is_valid": true,
        "total_count": 1000,
        "remaining_count": 950
      },
      {
        "couponId": 1004,
        "type": "折扣",
        "name": "新品折扣券",
        "description": "新品8折优惠",
        "discount_value": 0.8,
        "min_order_amount": 100.00,
        "start_time": "2023-06-15 00:00:00",
        "end_time": "2023-06-30 23:59:59",
        "is_valid": true,
        "total_count": 500,
        "remaining_count": 500
      }
    ]
  }
  ```

### 7. 分页查询优惠券

- **URL**: `/coupon/selectPage`
- **方法**: GET
- **描述**: 分页查询优惠券信息，支持条件筛选
- **请求参数**:

  | 参数名 | 类型 | 必选 | 描述 |
  | ------ | ------ | ------ | ------ |
  | pageNum | Integer | 否 | 页码，默认为1 |
  | pageSize | Integer | 否 | 每页条数，默认为10 |
  | type | String | 否 | 优惠券类型 |
  | name | String | 否 | 优惠券名称（模糊查询） |
  | is_valid | Boolean | 否 | 是否有效 |

- **请求示例**:

  ```
  GET /coupon/selectPage?pageNum=1&pageSize=10&type=满减&is_valid=true
  ```

- **响应参数**:

  | 参数名 | 类型 | 描述 |
  | ------ | ------ | ------ |
  | code | Integer | 状态码，1表示成功，0表示失败 |
  | msg | String | 提示信息 |
  | data | Object | 分页结果 |
  | data.total | Long | 总记录数 |
  | data.items | Array | 优惠券列表 |

- **响应示例**:

  ```json
  {
    "code": 1,
    "msg": "操作成功",
    "data": {
      "total": 5,
      "items": [
        {
          "couponId": 1001,
          "type": "满减",
          "name": "618满减券",
          "description": "618活动专享满200减50",
          "discount_value": 50.00,
          "min_order_amount": 200.00,
          "start_time": "2023-06-10 00:00:00",
          "end_time": "2023-06-20 23:59:59",
          "is_valid": true,
          "total_count": 1000,
          "remaining_count": 950
        },
        {
          "couponId": 1005,
          "type": "满减",
          "name": "父亲节满减券",
          "description": "父亲节满300减80",
          "discount_value": 80.00,
          "min_order_amount": 300.00,
          "start_time": "2023-06-15 00:00:00",
          "end_time": "2023-06-18 23:59:59",
          "is_valid": true,
          "total_count": 800,
          "remaining_count": 800
        }
      ]
    }
  }
  ```

## 错误码说明

| 错误码 | 描述 |
| ------ | ------ |
| 422 | 操作失败 |
| 200 | 操作成功 |

## 注意事项

1. 所有接口在请求失败时都会返回相应的错误信息
2. 优惠券的时间格式为 "YYYY-MM-DD HH:MM:SS"
3. 优惠券删除操作可能会影响关联的发放记录，请谨慎操作
4. 管理员相关接口需要管理员权限 