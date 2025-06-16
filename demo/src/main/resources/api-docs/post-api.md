# 帖子管理API文档

## 1. 获取圈子帖子列表

**URL**: `/api/circles/{circleId}/posts`  
**Method**: `GET`  
**Description**: 获取指定圈子下的所有帖子

**Path Parameters**:
- `circleId`: 圈子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "circleId": 1,
      "username": "用户1",
      "avatar": "头像URL",
      "content": "帖子内容",
      "postTime": "2025-06-16T16:43:43",
      "comments": []
    }
  ]
}
```

## 2. 获取帖子详情

**URL**: `/api/circles/{circleId}/post/{postId}`  
**Method**: `GET`  
**Description**: 获取帖子详细信息（含评论）

**Path Parameters**:
- `circleId`: 圈子ID
- `postId`: 帖子ID

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "circleId": 1,
    "username": "用户1",
    "avatar": "头像URL",
    "content": "帖子内容",
    "postTime": "2025-06-16T16:43:43",
    "comments": [
      {
        "id": 1,
        "content": "评论内容",
        "username": "评论用户"
      }
    ]
  }
}
```

## 3. 创建帖子

**URL**: `/api/circles/{circleId}/posts`  
**Method**: `POST`  
**Description**: 在指定圈子下创建新帖子

**Path Parameters**:
- `circleId`: 圈子ID

**Request Body**:
```json
{
  "username": "发帖用户",
  "avatar": "头像URL",
  "content": "帖子内容"
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

## 4. 创建评论

**URL**: `/api/circles/{circleId}/posts/{postId}/comments`  
**Method**: `POST`  
**Description**: 在指定帖子下创建评论

**Path Parameters**:
- `circleId`: 圈子ID
- `postId`: 帖子ID

**Request Body**:
```json
{
  "username": "评论用户",
  "content": "评论内容"
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
| 404 | 帖子不存在 |
| 500 | 服务器内部错误 |