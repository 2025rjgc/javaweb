# 用户管理API文档

## 1. 用户登录

**URL**: `/user/login`  
**Method**: `POST`  
**Description**: 用户登录接口

**Query Parameters**:
- `username`: 用户名 (必填)
- `password`: 密码 (必填)

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "username": "testuser",
    "token": "JWT_TOKEN_STRING"
  }
}
```

## 2. 用户注册

**URL**: `/user/register`  
**Method**: `POST`  
**Description**: 用户注册接口

**Request Body**:
```json
{
  "username": "新用户名",
  "password": "密码",
  "email": "邮箱",
  "phone": "电话",
  "personId": "学工号"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 2,
    "username": "新用户名"
  }
}
```

## 3. 查询用户信息

**URL**: `/user/getUserInfo`  
**Method**: `POST`  
**Description**: 根据条件查询用户信息

**Request Body**:
```json
{
  "username": "要查询的用户名",
  "role": 0
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "userId": 1,
      "username": "testuser",
      "email": "test@example.com",
      "phone": "13800138000",
      "avatar": "avatar_url.jpg"
    }
  ]
}
```

## 4. 更新用户信息

**URL**: `/user/updateUserInfo`  
**Method**: `POST`  
**Description**: 更新用户信息

**Request Body**:
```json
{
  "userId": 1,
  "email": "new@example.com",
  "phone": "13900139000",
  "signature": "新的个性签名"
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

## 5. 删除用户

**URL**: `/user/deleteUser`  
**Method**: `POST`  
**Description**: 删除用户

**Query Parameters**:
- `username`: 要删除的用户名 (必填)

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 6. 上传用户头像

**URL**: `/user/setImage`  
**Method**: `POST`  
**Description**: 上传用户头像

**Form Data**:
- `file`: 图片文件 (仅支持PNG/JPG/JPEG，最大5MB)
- `filename`: 文件名格式为"用户ID_自定义名称.扩展名"

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": "上传成功"
}
```

## 7. 获取用户头像

**URL**: `/user/image/{fileName}`  
**Method**: `GET`  
**Description**: 获取用户头像

**Path Parameters**:
- `fileName`: 头像文件名 (如"1_avatar.jpg")

**Response**: 直接返回图片二进制流

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权/登录失败 |
| 403 | 权限不足 |
| 404 | 用户不存在 |
| 413 | 文件大小超过限制 |
| 415 | 不支持的图片类型 |
| 500 | 服务器内部错误 |