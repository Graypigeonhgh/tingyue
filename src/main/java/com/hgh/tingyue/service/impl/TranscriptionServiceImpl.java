package com.hgh.tingyue.service.impl;

import com.hgh.tingyue.entity.Transcription;
import com.hgh.tingyue.mapper.TranscriptionMapper;
import com.hgh.tingyue.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptionServiceImpl implements TranscriptionService {

    private final TranscriptionMapper transcriptionMapper;

    @Override
    public Transcription save(Long audioFileId, String content, Long userId) {
        Transcription transcription = new Transcription();
        transcription.setAudioFileId(audioFileId);
        transcription.setContent(content);
        transcription.setStatus("COMPLETED");
        transcription.setUserId(userId);

        transcriptionMapper.insert(transcription);
        log.info("转写记录已保存，ID：{}", transcription.getId());

        return transcription;
    }
}