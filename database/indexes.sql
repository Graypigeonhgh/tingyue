-- 用户表索引
CREATE INDEX idx_users_created_at ON users(created_at);

-- 音频文件表索引
CREATE INDEX idx_audio_files_created_at ON audio_files(created_at);
CREATE INDEX idx_audio_files_user_created ON audio_files(user_id, created_at);

-- 转写记录表索引
CREATE INDEX idx_transcriptions_created_at ON transcriptions(created_at);
CREATE INDEX idx_transcriptions_status ON transcriptions(status);

-- 翻译记录表索引
CREATE INDEX idx_translations_created_at ON translations(created_at);
CREATE INDEX idx_translations_user_created ON translations(user_id, created_at);