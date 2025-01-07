-- 创建数据库
CREATE DATABASE IF NOT EXISTS tingyue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tingyue;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 音频文件表
CREATE TABLE IF NOT EXISTS audio_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '音频文件ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size INT NOT NULL COMMENT '文件大小(字节)',
    duration INT COMMENT '音频时长(秒)',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    source_type VARCHAR(20) NOT NULL COMMENT '来源类型(录音/导入)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='音频文件表';

-- 转写记录表
CREATE TABLE IF NOT EXISTS transcriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '转写记录ID',
    audio_file_id BIGINT NOT NULL COMMENT '音频文件ID',
    text_content TEXT COMMENT '转写文本内容',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    status VARCHAR(20) NOT NULL COMMENT '转写状态(进行中/完成/失败)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_audio_file_id (audio_file_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转写记录表';

-- 翻译记录表
CREATE TABLE IF NOT EXISTS translations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '翻译记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    original_text TEXT NOT NULL COMMENT '原文',
    translated_text TEXT NOT NULL COMMENT '译文',
    source_language VARCHAR(10) NOT NULL COMMENT '源语言',
    target_language VARCHAR(10) NOT NULL COMMENT '目标语言',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='翻译记录表'; 