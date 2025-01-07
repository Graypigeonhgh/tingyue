# TingYue API 文档

## 基础信息

- 基础URL: `http://localhost:8899`
- 认证方式: Bearer Token
- 响应格式: JSON

## 认证说明

除了注册和登录接口，所有API都需要在请求头中携带JWT token：
```http
Authorization: Bearer your_jwt_token
```

## 接口列表

### 用户管理

#### 1. 用户注册
```http
POST /api/user/register
Content-Type: application/json

{
    "username": "string",     // 用户名，必填，长度5-20
    "password": "string",     // 密码，必填，长度6-20
    "email": "string"        // 邮箱，必填，有效的邮箱格式
}
```

响应示例：
```json
{
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "createdAt": "2024-01-09T10:00:00"
}
```

#### 2. 用户登录
```http
POST /api/user/login
Content-Type: application/json

{
    "username": "string",     // 用户名，必填
    "password": "string"      // 密码，必填
}
```

响应示例：
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",  // JWT token
    "tokenType": "Bearer"
}
```

### 音频处理

#### 1. 上传音频并转写
```http
POST /api/audio/upload-and-transcribe
Content-Type: multipart/form-data

file: [音频文件]  // 支持的格式：MP3、WAV、M4A等
```

响应示例：
```json
{
    "text": "转写的文本内容"
}
```

请求限制：
- 文件大小上限：1024MB
- 支持的音频格式：audio/*
- 需要认证

#### 2. 实时语音识别
```http
POST /api/audio/recognize/stream
Accept: text/event-stream

duration: 30000  // 可选，录音时长（毫秒），默认30000
```

响应格式（SSE）：
```
event: partial
data: {"text": "部分识别结果"}

event: final
data: {"text": "最终识别结果"}
```

注意事项：
- 使用SSE（Server-Sent Events）技术
- 需要支持EventSource的客户端
- 连接会在指定duration后自动关闭

### 翻译服务

#### 1. 文本翻译
```http
POST /api/translation/translate
Content-Type: application/json

{
    "text": "string",           // 待翻译文本，必填
    "sourceLanguage": "string", // 源语言代码，必填（如：en、zh）
    "targetLanguage": "string"  // 目标语言代码，必填（如：zh、en）
}
```

响应示例：
```json
{
    "translatedText": "翻译后的文本",
    "sourceLanguage": "en",
    "targetLanguage": "zh"
}
```

支持的语言代码：
- zh: 中文
- en: 英文
- ja: 日语
- ko: 韩语
- fr: 法语
- de: 德语
- es: 西班牙语
- ru: 俄语

## 错误处理

所有接口的错误响应格式统一为：
```json
{
    "code": "ERROR_CODE",
    "message": "错误描述信息"
}
```

常见错误码：
- 400: 请求参数错误
- 401: 未认证或认证失败
- 403: 权限不足
- 404: 资源不存在
- 413: 请求体过大
- 415: 不支持的媒体类型
- 429: 请求过于频繁
- 500: 服务器内部错误

## 限流说明

- 登录接口: 60次/分钟/IP
- 音频转写: 10次/分钟/用户
- 实时识别: 5次/分钟/用户
- 文本翻译: 100次/分钟/用户

## 最佳实践

1. 音频上传
- 建议在上传前检查文件格式和大小
- 大文件建议使用分片上传
- 保存服务器返回的文件ID以便后续操作

2. 实时识别
- 建议使用WebSocket或EventSource实现
- 注意处理断线重连
- 合理设置超时时间

3. 翻译服务
- 建议对长文本进行分段翻译
- 注意保存翻译历史
- 合理处理特殊字符

## 更新日志

### v1.0.0 (2024-01-09)
- 初始版本发布
- 支持基础的音频转写和翻译功能
- 实现用户认证系统 

## API文档

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