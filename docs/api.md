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
    "token": "eyJhbGciOiJIUzI1NiJ9..."  // JWT token
}
```

### 音频文件管理

#### 1. 上传音频文件
```http
POST /api/audio-files/upload
Content-Type: multipart/form-data

file: [音频文件]  // 支持的格式：MP3、WAV、M4A等
```

响应示例：
```json
{
    "id": 1,
    "fileName": "test.mp3",
    "fileUrl": "https://xxx.oss-cn-xxx.aliyuncs.com/xxx.mp3",
    "fileSize": 1024567,
    "fileType": "audio/mpeg",
    "remark": null,
    "userId": 1,
    "createdAt": "2024-01-09T10:00:00",
    "updatedAt": "2024-01-09T10:00:00"
}
```

请求限制：
- 文件大小上限：1024MB（可配置）
- 支持的音频格式：MP3、WAV、M4A、AAC、FLAC等
- 需要认证

#### 2. 获取音频文件列表
```http
GET /api/audio-files?page=1&pageSize=10
```

参数说明：
- page: 页码，从1开始，默认1
- pageSize: 每页数量，默认10

响应示例：
```json
{
    "records": [
        {
            "id": 1,
            "fileName": "test.mp3",
            "fileUrl": "https://xxx.oss-cn-xxx.aliyuncs.com/xxx.mp3",
            "fileSize": 1024567,
            "fileType": "audio/mpeg",
            "remark": "这是一段重要的会议记录",
            "userId": 1,
            "createdAt": "2024-01-09T10:00:00",
            "updatedAt": "2024-01-09T10:00:00"
        }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
}
```

#### 3. 获取音频文件详情
```http
GET /api/audio-files/{id}
```

响应示例：
```json
{
    "audioFile": {
        "id": 1,
        "fileName": "test.mp3",
        "fileUrl": "https://xxx.oss-cn-xxx.aliyuncs.com/xxx.mp3",
        "fileSize": 1024567,
        "fileType": "audio/mpeg",
        "remark": "这是一段重要的会议记录",
        "userId": 1,
        "createdAt": "2024-01-09T10:00:00",
        "updatedAt": "2024-01-09T10:00:00"
    },
    "transcription": {
        "id": 1,
        "audioFileId": 1,
        "content": "转写的文本内容...",
        "status": "COMPLETED",
        "userId": 1,
        "createdAt": "2024-01-09T10:01:00",
        "updatedAt": "2024-01-09T10:01:00"
    }
}
```

说明：
- audioFile：音频文件的基本信息，包含备注字段
- transcription：音频文件的转写记录，如果还未转写则为 null
- status 可能的值：
  - PENDING：等待转写
  - PROCESSING：转写中
  - COMPLETED：转写完成
  - FAILED：转写失败

#### 4. 删除音频文件
```http
DELETE /api/audio-files/{id}
```

响应示例：
```json
{
    "success": true
}
```

说明：
- 删除音频文件会同时删除OSS中的文件和数据库记录
- 只能删除自己上传的文件
- 删除成功返回 true，失败抛出异常

#### 5. 更新音频文件信息
```http
PUT /api/audio-files/{id}
Content-Type: application/json

{
    "fileName": "新的文件名.mp3"     // 可选，新的文件名，最大255字符
}
```

响应示例：
```json
{
    "id": 1,
    "fileName": "新的文件名.mp3",
    "fileUrl": "https://xxx.oss-cn-xxx.aliyuncs.com/xxx.mp3",
    "fileSize": 1024567,
    "fileType": "audio/mpeg",
    "userId": 1,
    "createdAt": "2024-01-09T10:00:00",
    "updatedAt": "2024-01-09T10:00:00"
}
```

说明：
- 文件名是可选的
- 文件名长度不能超过255个字符
- 只能修改自己上传的文件
- 更新成功后返回完整的文件信息
- 不允许修改文件URL、大小等核心信息

### 音频转写

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
- 文件大小上限：100MB
- 支持的音频格式：MP3、WAV、M4A、AAC、FLAC等
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
- 采样率要求：16kHz
- 音频格式：PCM

### 转写记录管理

#### 1. 获取转写记录列表
```http
GET /api/transcriptions
Content-Type: application/json

page: 1       // 页码，默认1
pageSize: 10  // 每页数量，默认10
```

响应示例：
```json
{
    "records": [
        {
            "id": 1,
            "audioFileId": 1,
            "content": "转写的文本内容",
            "status": "COMPLETED",
            "userId": 1,
            "createdAt": "2024-01-09T10:00:00",
            "updatedAt": "2024-01-09T10:00:00"
        }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
}
```

#### 2. 获取转写记录详情
```http
GET /api/transcriptions/{id}
```

响应示例：
```json
{
    "id": 1,
    "audioFileId": 1,
    "content": "转写的文本内容",
    "status": "COMPLETED",
    "userId": 1,
    "createdAt": "2024-01-09T10:00:00",
    "updatedAt": "2024-01-09T10:00:00"
}
```

#### 3. 删除转写记录
```http
DELETE /api/transcriptions/{id}
```

响应示例：
```json
{
    "success": true
}
```

### 翻译服务

#### 1. 文本翻译
```http
POST /api/translation/translate

{
    "text": "string",           // 待翻译文本，必填
    "sourceLanguage": "string", // 源语言代码，必填
    "targetLanguage": "string"  // 目标语言代码，必填
}
```

响应示例：
```json
{
    "text": "翻译后的文本"
}
```

#### 2. 获取支持的语言列表
```http
GET /api/translation/languages
```

响应示例：
```json
{
    "zh": "中文",
    "en": "英语",
    "ja": "日语",
    "ko": "韩语",
    "fr": "法语",
    "de": "德语",
    "es": "西班牙语",
    "ru": "俄语"
}
```

## 错误响应
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

### 1. 音频上传
- 上传前检查文件格式和大小
- 大文件（>10MB）建议使用分片上传
- 保存服务器返回的文件ID以便后续操作
- 建议在客户端进行音频格式和采样率的预处理

### 2. 实时识别
- 使用WebSocket或EventSource实现
- 实现断线重连机制
- 合理设置超时时间（建议30-60秒）
- 注意处理网络延迟和抖动

### 3. 转写记录
- 定期清理无用的转写记录
- 实现转写记录的导出功能
- 提供转写文本的编辑功能
- 支持转写结果的分享功能

### 4. 翻译服务
- 对长文本进行分段翻译
- 缓存常用翻译结果
- 合理处理特殊字符和标点符号
- 支持批量翻译功能