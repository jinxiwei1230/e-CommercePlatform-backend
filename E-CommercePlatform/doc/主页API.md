# 主页接口文档

## 1. 获取首页轮播展示的热门商品

### 接口描述

获取用于首页轮播展示的热门商品列表。

### 请求信息

- **URL**: `/api/products/hot`

- **方法**: `GET`

- 请求参数：

  | 参数名 | 类型 | 是否必填 | 默认值 | 描述               |
  | ------ | ---- | -------- | ------ | ------------------ |
  | limit  | int  | 否       | 5      | 查询的热门商品数量 |

### 响应信息

- **状态码**: 200 OK

- **响应格式**: JSON

- 响应数据结构：

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "productId": number,           // 商品ID
        "productName": string,         // 商品名称
        "mainImageUrl": string|null,   // 商品主图URL，可能为null
        "sellingPrice": number,        // 销售价格
        "briefDescription": string,    // 简短描述
        "sales": number               // 销量
      }
    ]
  }
  ```

- 示例响应:

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "productId": 66,
        "productName": "瓶装矿泉水",
        "mainImageUrl": "/images/食品饮料0/饮品3/66矿泉水61.jpg",
        "sellingPrice": 1.90,
        "briefDescription": "纯净清爽，随时补水",
        "sales": 3000
      },
      {
        "productId": 61,
        "productName": "可乐碳酸饮料",
        "mainImageUrl": "/images/食品饮料0/饮品3/61碳酸饮料61.webp",
        "sellingPrice": 3.90,
        "briefDescription": "冰爽解渴，经典口味",
        "sales": 2000
      }
    ]
  }
  ```

### 错误响应

- **422 Bad Request**: 参数错误，资源不存在等
- **500 Internal Server Error**: 服务器内部错误。

------

## 2. 获取热门类别及其热门商品

### 接口描述

获取4个热门类别及其各自的5个热门商品。

### 请求信息

- **URL**: `/api/products/categories/hot`
- **方法**: `GET`
- **请求参数**: 无

### 响应信息

- **状态码**: 200 OK

- **响应格式**: JSON

- 响应数据结构:

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "categoryId": number,          // 类别ID
        "categoryName": string,        // 类别名称
        "hotProducts": [
          {
            "productId": number,       // 商品ID
            "productName": string,     // 商品名称
            "mainImageUrl": string|null, // 商品主图URL，可能为null
            "sellingPrice": number,    // 销售价格
            "briefDescription": string, // 简短描述
            "sales": number           // 销量
          }
        ]
      }
    ]
  }
  ```

- 示例响应:

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "categoryId": 3,
        "categoryName": "食品饮料",
        "hotProducts": [
          {
            "productId": 66,
            "productName": "瓶装矿泉水",
            "mainImageUrl": "/images/食品饮料0/饮品3/66矿泉水61.jpg",
            "sellingPrice": 1.90,
            "briefDescription": "纯净清爽，随时补水",
            "sales": 3000
          },
          {
            "productId": 61,
            "productName": "可乐碳酸饮料",
            "mainImageUrl": "/images/食品饮料0/饮品3/61碳酸饮料61.webp",
            "sellingPrice": 3.90,
            "briefDescription": "冰爽解渴，经典口味",
            "sales": 2000
          }
        ]
      },
      {
        "categoryId": 1,
        "categoryName": "服装配饰",
        "hotProducts": [
          {
            "productId": 14,
            "productName": "纯棉内裤",
            "mainImageUrl": null,
            "sellingPrice": 29.90,
            "briefDescription": "透气吸汗，健康选择",
            "sales": 1000
          }
        ]
      }
    ]
  }
  ```

------



## 3. 获取所有非叶子分类（树形结构）

### 接口描述

获取所有非叶子分类，包含子分类的树形结构。

### 请求信息

- **URL**: `/api/category/categories`
- **方法**: `GET`
- **请求参数**: 无

### 响应信息

- **状态码**: 200 OK

- **响应格式**: JSON

- 响应数据结构:

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "categoryId": number,          // 分类ID
        "name": string,                // 分类名称
        "parentId": number,            // 父分类ID（0表示根分类）
        "createTime": string|null,     // 创建时间，可能为null
        "children": [                  // 子分类列表
          {
            "categoryId": number,
            "name": string,
            "parentId": number,
            "createTime": string|null,
            "children": null           // 叶子节点无子分类
          }
        ]|null
      }
    ]
  }
  ```

- 示例响应:

  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "categoryId": 1,
        "name": "服装配饰",
        "parentId": 0,
        "createTime": null,
        "children": [
          {
            "categoryId": 7,
            "name": "男装",
            "parentId": 1,
            "createTime": null,
            "children": null
          },
          {
            "categoryId": 8,
            "name": "女装",
            "parentId": 1,
            "createTime": null,
            "children": null
          },
          {
            "categoryId": 9,
            "name": "童装",
            "parentId": 1,
            "createTime": null,
            "children": null
          }
        ]
      },
      {
        "categoryId": 2,
        "name": "美妆护肤",
        "parentId": 0,
        "createTime": null,
        "children": [
          {
            "categoryId": 42,
            "name": "化妆品",
            "parentId": 2,
            "createTime": null,
            "children": null
          },
          {
            "categoryId": 43,
            "name": "护肤品",
            "parentId": 2,
            "createTime": null,
            "children": null
          }
        ]
      }
    ]
  }
  ```



------

## 注意事项

- 所有接口返回的`mainImageUrl`字段（适用于产品相关接口）可能为`null`，表示该商品暂无主图。
- 响应中的`code`字段为200表示成功，其他值表示错误，具体错误信息在`message`字段中。
- 分类树形结构中，`children`字段为`null`表示该分类为叶子节点，无子分类。