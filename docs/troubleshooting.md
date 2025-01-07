# 故障排除指南

## 音频转写问题

### 1. 音频文件要求
在使用音频转写功能之前，请确保您的音频文件符合以下要求：

- **支持的音频格式**：WAV、MP3、M4A等
- **采样率**：16kHz
- **位深度**：16bit
- **声道数**：单声道
- **文件大小**：建议不超过100MB

### 2. 常见错误及解决方案

#### 2.1 OSS访问错误
**错误信息**：
```
音频文件不存在：https://xxx.oss-cn-xxx.aliyuncs.com/xxx.mp3
```

**可能原因**：
- OSS文件URL无法访问
- 文件未成功上传
- Bucket访问权限配置错误

**解决方案**：
1. 检查OSS Bucket配置
   - 确认Bucket是否设置为公共读取
   - 验证Bucket名称是否正确
   - 检查Endpoint配置是否正确

2. 验证文件上传
   - 在OSS控制台确认文件是否存在
   - 尝试直接在浏览器访问文件URL
   - 检查文件权限设置

3. 检查网络连接
   - 确保服务器可以访问阿里云OSS
   - 验证防火墙设置

#### 2.2 转写结果解析错误
**错误信息**：
```
Cannot invoke "com.google.gson.JsonElement.getAsString()" because the return value of "com.google.gson.JsonObject.get(String)" is null
```

**可能原因**：
- 音频文件格式不符合要求
- 音频文件内容无效
- 转写服务返回的JSON格式异常

**解决方案**：
1. 检查音频文件
   - 确认文件格式是否支持
   - 验证音频文件是否可以正常播放
   - 检查音频文件的编码格式

2. 查看转写任务状态
   - 检查任务ID是否有效
   - 查看详细的错误信息
   - 验证转写结果的完整性

3. 调试步骤
   ```java
   // 添加详细的日志记录
   log.debug("转写结果URL: {}", transcriptionUrl);
   log.debug("转写结果JSON: {}", jsonResult);
   ```

#### 2.3 API密钥错误
**错误信息**：
```
未配置阿里云API密钥
```

**解决方案**：
1. 检查配置文件
   ```yaml
   # application-local.yml
   aliyun:
     dashscope:
       api-key: ${DASHSCOPE_API_KEY:your-api-key}
   ```

2. 检查环境变量
   ```bash
   # Linux/Mac
   export DASHSCOPE_API_KEY=your-api-key
   
   # Windows
   set DASHSCOPE_API_KEY=your-api-key
   ```

3. 验证API密钥
   - 确保API密钥有效且未过期
   - 检查API密钥权限是否足够

### 3. 转写结果格式说明

#### 3.1 转写结果JSON格式示例
```json
{
  "file_url": "https://example.com/audio.mp3",
  "properties": {
    "audio_format": "mp3",
    "channels": [0, 1],
    "original_sampling_rate": 32000,
    "original_duration_in_milliseconds": 1368424
  },
  "transcripts": [
    {
      "channel_id": 0,
      "content_duration_in_milliseconds": 1037100,
      "text": "转写的文本内容...",
      "sentences": [
        {
          "begin_time": 60,
          "end_time": 12045,
          "text": "句子内容...",
          "sentence_id": 1,
          "words": [...]
        }
      ]
    }
  ]
}
```

#### 3.2 字段说明
- `file_url`: 音频文件的URL
- `properties`: 音频文件的属性
  - `audio_format`: 音频格式
  - `channels`: 声道数
  - `original_sampling_rate`: 原始采样率
  - `original_duration_in_milliseconds`: 原始时长（毫秒）
- `transcripts`: 转写结果数组
  - `channel_id`: 声道ID
  - `content_duration_in_milliseconds`: 文本持续时间（毫秒）
  - `text`: 转写的文本内容
  - `sentences`: 转写结果句子数组
    - `begin_time`: 句子开始时间（毫秒）
    - `end_time`: 句子结束时间（毫秒）
    - `text`: 转写的文本内容
    - `sentence_id`: 句子ID
    - `words`: 单词数组

## 实时语音识别问题

### 1. 麦克风访问问题

**常见问题**：
- 浏览器无法访问麦克风
- 音频设备未正确识别
- 其他应用占用麦克风

**解决方案**：
1. 浏览器设置
   - 允许网站访问麦克风
   - 检查浏览器版本是否支持
   - 清除浏览器缓存

2. 系统设置
   - 检查音频设备是否正常工作
   - 确认默认录音设备设置
   - 关闭其他使用麦克风的应用

### 2. WebSocket连接问题

**常见问题**：
- 连接建立失败
- 连接意外断开
- 数据传输超时

**解决方案**：
1. 网络检查
   - 验证网络连接稳定性
   - 检查防火墙设置
   - 确认代理配置

2. 服务器配置
   - 检查WebSocket端口是否开放
   - 验证SSL证书配置
   - 调整连接超时设置

## 性能优化建议

### 1. 大文件处理
- 实现分片上传
- 使用断点续传
- 添加进度显示

### 2. 资源管理
- 及时清理临时文件
- 实现文件大小限制
- 添加文件类型验证

### 3. 错误处理
- 实现优雅降级
- 添加重试机制
- 完善错误提示

## 安全注意事项

### 1. API密钥保护
- 避免在代码中硬编码
- 使用环境变量或配置中心
- 定期轮换密钥

### 2. 文件安全
- 验证文件类型
- 限制文件大小
- 实现防盗链措施

### 3. 访问控制
- 实现用户认证
- 添加操作审计
- 控制资源访问权限 