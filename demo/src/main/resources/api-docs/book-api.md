# 图书管理API文档

## 1. 获取所有图书信息

**URL**: `/book`  
**Method**: `GET`  
**Description**: 获取系统中所有图书信息

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "bookId": 1,
      "bookName": "示例图书",
      "bookPre": "图书简介",
      "author": "作者名",
      "publisher": "出版社",
      "genre": "类别",
      "bookImg": "封面图片URL",
      "bookUrl": "图书链接",
      "created_at": "2025-06-16T16:39:21",
      "updated_at": "2025-06-16T16:39:21"
    }
  ]
}
```

## 2. 多条件查询图书

**URL**: `/book/search`  
**Method**: `POST`  
**Description**: 根据条件查询图书

**Request Body**:
```json
{
  "bookName": "示例图书",
  "author": "作者名",
  "publisher": "出版社"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "bookId": 1,
      "bookName": "示例图书",
      "author": "作者名",
      "publisher": "出版社"
    }
  ]
}
```

## 3. 删除图书

**URL**: `/book/{id}`  
**Method**: `DELETE`  
**Description**: 删除指定ID的图书

**Path Parameters**:
- `id`: 图书ID

**Response**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

## 4. 添加图书

**URL**: `/book/add`  
**Method**: `POST`  
**Description**: 添加新图书

**Request Body**:
```json
{
  "bookName": "新图书",
  "bookPre": "图书简介",
  "author": "作者名",
  "publisher": "出版社",
  "genre": "类别",
  "bookImg": "封面图片URL",
  "bookUrl": "图书链接"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

## 5. 更新图书信息

**URL**: `/book`  
**Method**: `POST`  
**Description**: 更新图书信息

**Request Body**:
```json
{
  "bookId": 1,
  "bookName": "更新后的图书名",
  "author": "更新后的作者"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

## 6. 获取图书封面图片

**URL**: `/book/image/{fileName}`  
**Method**: `GET`  
**Description**: 获取图书封面图片

**Path Parameters**:
- `fileName`: 图片文件名(支持.jpg/.png/.gif)

**Response**: 图片二进制数据

## 7. 获取图书介绍文本

**URL**: `/book/txt/{fileName}`  
**Method**: `GET`  
**Description**: 获取图书介绍文本内容

**Path Parameters**:
- `fileName`: 文本文件名

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": "图书介绍文本内容..."
}
```

## 错误码说明

| 错误码 | 说明      |
|-----|---------|
| 400 | 请求参数错误  |
| 404 | 资源不存在   |
| 500 | 服务器内部错误 |