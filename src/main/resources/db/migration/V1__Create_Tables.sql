-- 音频文件表
CREATE TABLE IF NOT EXISTS `audio_files` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_url` VARCHAR(1024) NOT NULL COMMENT '文件URL',
    `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
    `file_type` VARCHAR(100) NOT NULL COMMENT '文件类型',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
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