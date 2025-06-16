# 圈子管理API文档

## 1. 获取所有圈子

**URL**: `/api/circles`  
**Method**: `GET`  
**Description**: 获取所有圈子信息

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "示例圈子",
      "owner": "创建者",
      "cover": "封面URL",
      "members": 10,
      "posts": 5
    }
  ]
}
```

## 2. 获取指定圈子

**URL**: `/api/circles/{id}`  
**Method**: `GET`  
**Description**: 获取指定ID的圈子信息

**Path Parameters**:
- `id`: 圈子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "示例圈子",
    "owner": "创建者",
    "cover": "封面URL",
    "members": 10,
    "posts": 5
  }
}
```

## 3. 创建圈子

**URL**: `/api/circles`  
**Method**: `POST`  
**Description**: 创建新圈子

**Request Body**:
```json
{
  "title": "新圈子",
  "owner": "创建者",
  "cover": "封面URL"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 4. 删除圈子

**URL**: `/api/circles/{id}`  
**Method**: `DELETE`  
**Description**: 删除指定ID的圈子

**Path Parameters**:
- `id`: 圈子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 5. 获取圈子成员

**URL**: `/api/circles/{id}/members`  
**Method**: `GET`  
**Description**: 获取指定圈子的成员列表

**Path Parameters**:
- `id`: 圈子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "userId": 1,
      "userName": "成员1"
    }
  ]
}
```

## 6. 获取可邀请用户

**URL**: `/api/circles/{id}/users`  
**Method**: `GET`  
**Description**: 获取可邀请加入圈子的用户列表

**Path Parameters**:
- `id`: 圈子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "userId": 2,
      "userName": "可邀请用户"
    }
  ]
}
```

## 7. 邀请成员

**URL**: `/api/circles/{id}/invite`  
**Method**: `POST`  
**Description**: 邀请用户加入圈子

**Path Parameters**:
- `id`: 圈子ID

**Request Body**:
```json
{
  "userId": 2
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 404 | 圈子不存在 |
| 500 | 服务器内部错误 |