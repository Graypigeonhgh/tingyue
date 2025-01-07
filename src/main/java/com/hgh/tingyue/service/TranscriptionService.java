package com.hgh.tingyue.service;

import com.hgh.tingyue.entity.Transcription;

public interface TranscriptionService {
    /**
     * 保存转写记录
     *
     * @param audioFileId 音频文件ID
     * @param content     转写内容
     * @param userId      用户ID
     * @return 保存的转写记录
     */
    Transcription save(Long audioFileId, String content, Long userId);
}