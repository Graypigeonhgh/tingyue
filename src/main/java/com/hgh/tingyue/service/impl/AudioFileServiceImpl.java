package com.hgh.tingyue.service.impl;

import com.hgh.tingyue.entity.AudioFile;
import com.hgh.tingyue.mapper.AudioFileMapper;
import com.hgh.tingyue.service.AudioFileService;
import com.hgh.tingyue.service.OssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioFileServiceImpl implements AudioFileService {

    private final OssService ossService;
    private final AudioFileMapper audioFileMapper;

    @Override
    @Transactional
    public AudioFile uploadAndSave(MultipartFile file, Long userId) {
        // 上传文件到OSS
        String fileUrl = ossService.uploadFile(file);
        log.info("文件已上传到OSS：{}", fileUrl);

        // 保存文件记录
        AudioFile audioFile = new AudioFile();
        audioFile.setFileName(file.getOriginalFilename());
        audioFile.setFileSize(file.getSize());
        audioFile.setFileType(file.getContentType());
        audioFile.setFileUrl(fileUrl);
        audioFile.setUserId(userId);

        audioFileMapper.insert(audioFile);
        log.info("音频文件记录已保存，ID：{}", audioFile.getId());

        return audioFile;
    }
}