# 听悦 - 音频转笔记工具

## 项目介绍
听悦是一款音频转笔记工具网站的后端接口部分，提供对音频文件进行转译和笔记化的功能。使用阿里云百炼的SenseVoice语音识别大模型进行语音识别。

## 快速开始

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤
1. 克隆项目
```bash
git clone https://github.com/yourusername/tingyue.git
cd tingyue
```

2. 配置数据库
```bash
# 创建数据库和表结构
mysql -u root -p < database/schema.sql
# 创建索引
mysql -u root -p < database/indexes.sql
```

3. 配置环境变量
```bash
export DASHSCOPE_API_KEY=your-api-key
export OSS_ACCESS_KEY_ID=your-access-key-id
export OSS_ACCESS_KEY_SECRET=your-access-key-secret
```

4. 修改配置文件
复制`application.yml.example`为`application.yml`，并修改相关配置

5. 运行项目
```bash
mvn spring-boot:run
```

## 已完成功能

### 1. 基础架构搭建
- [x] 项目基础框架搭建
- [x] 数据库表结构设计
- [x] MyBatis-Plus集成
- [x] 阿里云OSS集成
- [x] 阿里云SenseVoice集成
- [x] 数据访问层实现

### 2. 音频文件处理
- [x] 音频文件上传功能
- [x] 文件格式验证
- [x] 文件存储到阿里云OSS
- [x] 音频文件转写接口对接
- [x] 音频文件数据持久化

## 待完成功能

### 1. 数据访问层
- [x] 用户数据访问接口
- [x] 音频文件数据访问接口
- [x] 转写记录数据访问接口

### 2. 用户功能
- [x] 用户注册
- [x] 用户登录
- [x] 用户认证授权

### 3. 音频处理功能
- [ ] 录音功能
- [ ] 音频转文本完整流程
- [x] 文本翻译功能
- [ ] 文本转语音功能

### 4. 其他功能
- [ ] 错误处理机制
- [ ] 日志记录
- [ ] 接口文档完善
- [ ] 单元测试

## 技术栈
- Spring Boot 2.6.13
- MyBatis-Plus 3.5.3
- MySQL
- 阿里云OSS
- 阿里云SenseVoice
- JWT
- Swagger

## 配置说明
项目运行需要配置以下环境变量：
- `DASHSCOPE_API_KEY`: 阿里云SenseVoice API密钥
- 阿里云OSS相关配置
  - endpoint
  - accessKeyId
  - accessKeySecret
  - bucketName

## API接口

### 用户接口

#### 1. 用户注册
```http
POST /api/user/register

请求参数：
- username: test123（示例）
- password: 123456（示例）
- email: test@example.com（示例）

响应示例：
{
    "id": 1,
    "username": "test123",
    "email": "test@example.com",
    "createdAt": "2024-01-09T10:00:00"
}
```

#### 2. 用户登录
```http
POST /api/user/login

请求参数：
- username: test123（示例）
- password: 123456（示例）

响应示例：
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 音频处理接口

#### 1. 上传音频
```http
POST /api/audio/upload

Content-Type: multipart/form-data

请求参数：
- file: 音频文件（必填，支持格式：mp3, wav, m4a等）
- sourceType: 来源类型（必填，可选值：RECORD/IMPORT）

响应示例：
{
    "id": 1,
    "fileName": "test.mp3",
    "fileSize": 1024000,
    "duration": 60,
    "fileType": "audio/mpeg",
    "sourceType": "IMPORT",
    "createdAt": "2024-01-09T10:00:00"
}
```

#### 2. 音频转写
```http
POST /api/audio/{audioId}/transcribe

请求参数：
- audioId: 音频文件ID（路径参数）

响应示例：
{
    "id": 1,
    "audioFileId": 1,
    "textContent": "这是转写的文本内容...",
    "language": "zh",
    "status": "COMPLETED",
    "createdAt": "2024-01-09T10:00:00"
}
```

#### 3. 文本翻译
```http
POST /api/translation/translate

请求参数：
- text: 待翻译文本（必填）
- sourceLanguage: 源语言（可选，默认zh）
- targetLanguage: 目标语言（必填）

响应示例：
{
    "originalText": "你好，世界",
    "translatedText": "Hello, World",
    "sourceLanguage": "zh",
    "sourceLangName": "中文",
    "targetLanguage": "en",
    "targetLangName": "英语"
}
```

#### 4. 获取支持的语言列表
```http
GET /api/translation/languages

响应示例：
{
    "zh": "中文",
    "en": "英语",
    "ja": "日语",
    "ko": "韩语",
    "fr": "法语",
    "es": "西班牙语",
    "ru": "俄语",
    "de": "德语",
    "it": "意大利语",
    "pt": "葡萄牙语"
}
```

### 认证说明
除了注册和登录接口，其他所有接口都需要在请求头中携带JWT令牌：
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 错误响应
当发生错误时，接口会返回统一格式的错误信息：
```json
{
    "code": 400,
    "message": "错误信息描述"
}
```

常见HTTP状态码：
- 200: 请求成功
- 400: 请求参数错误
- 401: 未授权或token无效
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

## 部署说明

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+

### API文档

### 访问地址
- 文档UI界面：http://localhost:8899/doc.html
- OpenAPI规范：http://localhost:8899/v3/api-docs

### 主要功能模块
1. 用户管理
   - 用户注册
   - 用户登录

2. 音频管理
   - 上传音频
   - 音频转写
   - 删除音频

3. 翻译管理
   - 文本翻译
   - 获取支持的语言列表

### 认证说明
除了注册和登录接口，其他接口都需要在请求头中携带JWT令牌：
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
