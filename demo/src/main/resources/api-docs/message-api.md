# 消息管理API文档

## 1. 新增消息

**URL**: `/message/newMessage`  
**Method**: `POST`  
**Description**: 新增一条消息

**Request Body**:
```json
{
  "userId": 1,
  "bookId": 1,
  "text": "消息内容"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "messageId": 1,
    "userId": 1,
    "bookId": 1,
    "text": "消息内容",
    "createTime": "2025-06-16T16:41:53",
    "updateTime": "2025-06-16T16:41:53"
  }
}
```

## 2. 获取用户消息

**URL**: `/message/getMessage`  
**Method**: `POST`  
**Description**: 获取指定用户的消息

**Request Parameters**:
- `userId`: 用户ID (必填)

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "messageId": 1,
      "userId": 1,
      "bookId": 1,
      "text": "消息内容1",
      "createTime": "2025-06-16T16:41:53"
    },
    {
      "messageId": 2,
      "userId": 1,
      "bookId": 2,
      "text": "消息内容2",
      "createTime": "2025-06-16T16:42:00"
    }
  ]
}
```

## 3. 删除消息

**URL**: `/message/delete`  
**Method**: `DELETE`  
**Description**: 删除指定消息

**Request Parameters**:
- `messageId`: 消息ID (必填)

**Response**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

## 4. 更新消息

**URL**: `/message/update`  
**Method**: `PUT`  
**Description**: 更新消息内容

**Request Body**:
```json
{
  "messageId": 1,
  "text": "更新后的消息内容"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "messageId": 1,
    "userId": 1,
    "bookId": 1,
    "text": "更新后的消息内容",
    "updateTime": "2025-06-16T16:43:00"
  }
}
```

## 5. 多条件查询消息

**URL**: `/message/search`  
**Method**: `GET`  
**Description**: 根据条件查询消息

**Request Parameters**:
- `userId`: 用户ID (可选)
- `bookId`: 图书ID (可选)
- `text`: 消息内容关键词 (可选)

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "messageId": 1,
      "userId": 1,
      "bookId": 1,
      "text": "包含关键词的消息",
      "createTime": "2025-06-16T16:41:53"
    }
  ]
}
```

## 错误码说明

| 错误码 | 说明      |
|-----|---------|
| 400 | 请求参数错误  |
| 404 | 消息不存在   |
| 500 | 服务器内部错误 |