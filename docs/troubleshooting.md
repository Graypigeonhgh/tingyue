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

## 跨域问题处理

### 1. 常见跨域错误
**错误信息**：
```
Access to XMLHttpRequest at 'http://localhost:8899/api/xxx' from origin 'http://localhost:3000' has been blocked by CORS policy
```

**可能原因**：
- 未配置 CORS
- CORS 配置不正确
- 前端请求配置问题
- 多重代理导致的 CORS 配置失效

### 2. 解决方案

#### 2.1 后端配置
1. 添加 CORS 配置类
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

2. 在 SecurityConfig 中配置 CORS
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOriginPattern("*");
            config.setAllowCredentials(true);
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            return config;
        }))
        // ... 其他配置
        .build();
}
```

3. 在 WebConfig 中配置 CORS
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
}
```

#### 2.2 前端配置
1. Axios 配置
```javascript
// 允许跨域携带认证信息
axios.defaults.withCredentials = true;

// 添加请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

2. Fetch API 配置
```javascript
fetch(url, {
  credentials: 'include',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
```

#### 2.3 Nginx 配置
如果使用 Nginx 作为反向代理，需要添加以下配置：
```nginx
location /api {
    add_header 'Access-Control-Allow-Origin' '*';
    add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS';
    add_header 'Access-Control-Allow-Headers' '*';
    add_header 'Access-Control-Allow-Credentials' 'true';
    
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Max-Age' 3600;
        add_header 'Content-Type' 'text/plain charset=UTF-8';
        add_header 'Content-Length' 0;
        return 204;
    }
    
    proxy_pass http://backend;
}
```

### 3. 注意事项

1. 安全考虑
   - 生产环境不要使用 `*` 通配符
   - 明确指定允许的域名
   - 限制必要的请求方法和请求头

2. 调试技巧
   - 使用浏览器开发者工具查看请求头和响应头
   - 检查 OPTIONS 预检请求是否正常
   - 验证 token 是否正确携带

3. 常见陷阱
   - 同时配置了多个 CORS 可能会冲突
   - 忘记配置 OPTIONS 请求的处理
   - withCredentials 与 Access-Control-Allow-Origin: * 冲突

4. 最佳实践
   - 使用环境变量配置允许的域名
   - 实现跨域请求的日志记录
   - 添加跨域相关的监控指标 

## Java 版本问题

### 1. Java 版本不兼容错误
**错误信息**：
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/springframework/boot/loader/launch/JarLauncher has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 52.0
```

**原因说明**：
- 项目使用 Java 17 编译
- 运行环境使用的是 Java 8
- Spring Boot 3.x 要求最低 Java 17

**解决方案**：

1. 升级 Java 运行环境
```bash
# 查看当前 Java 版本
java -version

# 安装 Java 17
# Windows: 下载并安装 JDK 17
# Linux:
sudo apt install openjdk-17-jdk  # Ubuntu
sudo yum install java-17-openjdk # CentOS

# 设置 JAVA_HOME
# Windows:
set JAVA_HOME=C:\Program Files\Java\jdk-17
# Linux:
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

2. 降级项目 Java 版本（不推荐）
   - 修改 pom.xml 中的 Java 版本
   ```xml
   <properties>
       <java.version>8</java.version>
   </properties>
   ```
   - 降级 Spring Boot 版本到 2.x
   ```xml
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.7.x</version>
   </parent>
   ```

3. 验证 Java 版本
```bash
# 查看编译版本
javac -version

# 查看运行版本
java -version

# 确保两者都是 Java 17
```

### 2. 常见问题

1. 多个 Java 版本共存
   - 使用 JAVA_HOME 环境变量指定正确版本
   - 使用版本管理工具（如 jenv）
   - 检查 PATH 环境变量中的 Java 路径顺序

2. IDE 配置
   - 确保 IDE 使用正确的 JDK 版本
   - 检查项目结构设置
   - 更新 Maven/Gradle 配置

3. CI/CD 环境
   - 检查构建环境的 Java 版本
   - 更新 Dockerfile 中的基础镜像
   - 配置正确的 Java 运行时环境

### 3. 最佳实践

1. 版本统一
   - 开发环境和生产环境使用相同的 Java 版本
   - 在 pom.xml 中明确指定 Java 版本
   - 使用 Maven 编译插件强制版本检查

2. 文档说明
   - 在 README.md 中注明所需的 Java 版本
   - 提供版本升级指南
   - 记录已知的版本兼容性问题

3. 环境检查
```bash
# 添加版本检查脚本
if [ "$(java -version 2>&1 | grep -i version | cut -d'"' -f2 | cut -d'.' -f1)" -lt "17" ]; then
    echo "Error: Java 17 or higher is required"
    exit 1
fi
``` 