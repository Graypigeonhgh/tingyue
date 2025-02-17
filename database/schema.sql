-- 创建数据库
CREATE DATABASE IF NOT EXISTS tingyue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tingyue;

-- 首先删除有外键依赖的表
DROP TABLE IF EXISTS edit_history;
DROP TABLE IF EXISTS translations;
DROP TABLE IF EXISTS transcriptions;
DROP TABLE IF EXISTS audio_files;
DROP TABLE IF EXISTS user;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 音频文件表
CREATE TABLE IF NOT EXISTS audio_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='音频文件表';

-- 转写记录表
CREATE TABLE IF NOT EXISTS transcriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    audio_file_id BIGINT NOT NULL COMMENT '音频文件ID',
    content TEXT NOT NULL COMMENT '转写内容',
    status VARCHAR(20) NOT NULL COMMENT '转写状态',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_audio_file_id (audio_file_id),
    INDEX idx_user_id (user_id)
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

ALTER TABLE audio_files
ADD COLUMN remark VARCHAR(500) DEFAULT NULL COMMENT '备注'; 