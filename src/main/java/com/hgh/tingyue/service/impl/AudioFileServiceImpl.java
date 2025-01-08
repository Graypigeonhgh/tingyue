package com.hgh.tingyue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgh.tingyue.entity.AudioFile;
import com.hgh.tingyue.entity.Transcription;
import com.hgh.tingyue.mapper.AudioFileMapper;
import com.hgh.tingyue.mapper.TranscriptionMapper;
import com.hgh.tingyue.service.AudioFileService;
import com.hgh.tingyue.service.OssService;
import com.hgh.tingyue.util.AudioUtils;
import com.hgh.tingyue.dto.response.AudioFileDetailResponse;
import com.hgh.tingyue.dto.request.AudioFileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioFileServiceImpl implements AudioFileService {
    private final AudioFileMapper audioFileMapper;
    private final OssService ossService;
    private final TranscriptionMapper transcriptionMapper;

    @Override
    @Transactional
    public AudioFile uploadAndSave(MultipartFile file, Long userId) {
        // 验证文件格式
        if (!AudioUtils.isValidAudioFile(file)) {
            throw new IllegalArgumentException("不支持的音频文件格式");
        }

        // 验证文件名长度
        if (file.getOriginalFilename() != null && file.getOriginalFilename().length() > 255) {
            throw new IllegalArgumentException("文件名过长，最大支持255个字符");
        }

        // 上传到OSS
        String fileUrl = ossService.uploadFile(file);
        if (fileUrl.length() > 500) { // schema.sql中定义的长度是500
            throw new IllegalArgumentException("文件URL过长，最大支持500个字符");
        }
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

    @Override
    @Transactional
    public boolean deleteFile(Long id, Long userId) {
        // 直接查询音频文件，不使用 getFileDetail
        AudioFile file = audioFileMapper.selectById(id);
        if (file == null || !file.getUserId().equals(userId)) {
            throw new RuntimeException("文件不存在或无权访问");
        }

        // 从OSS删除文件
        try {
            ossService.deleteFile(file.getFileUrl());
        } catch (Exception e) {
            log.error("从OSS删除文件失败：{}", file.getFileUrl(), e);
        }

        // 从数据库删除记录
        return audioFileMapper.deleteById(id) > 0;
    }

    @Override
    public IPage<AudioFile> getUserFiles(Long userId, int page, int pageSize) {
        Page<AudioFile> pageable = new Page<>(page, pageSize);
        return audioFileMapper.selectPage(pageable,
                new LambdaQueryWrapper<AudioFile>()
                        .eq(AudioFile::getUserId, userId)
                        .orderByDesc(AudioFile::getCreatedAt));
    }

    @Override
    public AudioFileDetailResponse getFileDetail(Long id, Long userId) {
        // 获取音频文件信息
        AudioFile file = audioFileMapper.selectById(id);
        if (file == null || !file.getUserId().equals(userId)) {
            throw new RuntimeException("文件不存在或无权访问");
        }

        // 获取转写记录
        Transcription transcription = transcriptionMapper.selectLatestByAudioFileId(id);

        // 封装响应
        AudioFileDetailResponse response = new AudioFileDetailResponse();
        response.setAudioFile(file);
        response.setTranscription(transcription);

        return response;
    }

    @Override
    @Transactional
    public AudioFile updateFile(Long id, Long userId, AudioFileUpdateRequest request) {
        // 获取音频文件
        AudioFile file = audioFileMapper.selectById(id);
        if (file == null || !file.getUserId().equals(userId)) {
            throw new RuntimeException("文件不存在或无权访问");
        }

        // 更新文件信息
        if (request.getFileName() != null) {
            file.setFileName(request.getFileName());
        }

        // 保存更新
        audioFileMapper.updateById(file);
        return file;
    }
}