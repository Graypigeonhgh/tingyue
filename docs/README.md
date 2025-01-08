# 听悦 - 音频转笔记工具

## 项目介绍
听悦是一款音频转笔记工具网站的后端接口部分，提供对音频文件进行转译和笔记化的功能。使用阿里云百炼的SenseVoice语音识别大模型进行语音识别。

## 主要功能
- 用户管理：注册、登录
- 音频管理：上传、删除、查看列表
- 音频转写：支持文件转写和实时语音识别
- 翻译功能：支持多语言互译

## 技术栈
- Spring Boot 3.x
- Spring Security + JWT
- MyBatis-Plus
- MySQL 8.x
- 阿里云OSS
- 阿里云百炼语音识别
- Knife4j (API文档)

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- 阿里云账号(用于OSS和语音识别服务)

### 安装步骤

1. 克隆项目
```bash
git clone https://github.com/yourusername/tingyue.git
cd tingyue
```

2. 配置数据库
```bash
# 创建数据库
mysql -u root -p < database/schema.sql
```

3. 配置环境变量
```bash
# 阿里云百炼API密钥
export DASHSCOPE_API_KEY=your-api-key

# 阿里云OSS配置
export OSS_ACCESS_KEY_ID=your-access-key-id
export OSS_ACCESS_KEY_SECRET=your-access-key-secret
export OSS_ENDPOINT=your-oss-endpoint
export OSS_BUCKET_NAME=your-bucket-name

# JWT配置
export JWT_SECRET=your-jwt-secret
export JWT_EXPIRATION=86400
```

4. 修改配置文件
复制`application.yml.example`为`application.yml`，并修改相关配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tingyue?useSSL=false
    username: your-username
    password: your-password

aliyun:
  oss:
    endpoint: ${OSS_ENDPOINT}
    accessKeyId: ${OSS_ACCESS_KEY_ID}
    accessKeySecret: ${OSS_ACCESS_KEY_SECRET}
    bucketName: ${OSS_BUCKET_NAME}
  dashscope:
    api-key: ${DASHSCOPE_API_KEY}

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION}
```

5. 运行项目
```bash
mvn spring-boot:run
```

6. 访问API文档
```
http://localhost:8899/doc.html
```

## 项目结构
```
src/main/java/com/hgh/tingyue/
├── config/          # 配置类
├── controller/      # 控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── mapper/         # MyBatis映射接口
├── service/        # 服务接口
│   └── impl/      # 服务实现
├── util/           # 工具类
└── TingyueApplication.java

src/main/resources/
├── mapper/         # MyBatis XML映射文件
├── db/migration/   # 数据库迁移脚本
└── application.yml # 配置文件
```

## 开发指南

### 添加新功能
1. 在entity包中创建实体类
2. 在mapper包中创建Mapper接口
3. 在service包中定义服务接口和实现
4. 在controller包中创建控制器
5. 在resources/mapper中添加XML映射文件(如需要)

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一使用统一响应格式
- 添加完整的接口文档注释

## 部署说明

### 打包
```bash
mvn clean package -DskipTests
```

### 运行
```bash
java -jar target/tingyue-0.0.1-SNAPSHOT.jar
```

### Docker部署
```bash
# 构建镜像
docker build -t tingyue .

# 运行容器
docker run -d -p 8899:8899 \
  -e DASHSCOPE_API_KEY=xxx \
  -e OSS_ACCESS_KEY_ID=xxx \
  -e OSS_ACCESS_KEY_SECRET=xxx \
  tingyue
```

## 贡献指南
1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建Pull Request

## 许可证
MIT License

## 联系方式
- 作者：Gray
- 邮箱：your-email@example.com
- GitHub：https://github.com/yourusername
