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
