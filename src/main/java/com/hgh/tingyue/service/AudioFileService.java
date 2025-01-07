package com.hgh.tingyue.service;

import com.hgh.tingyue.entity.AudioFile;
import org.springframework.web.multipart.MultipartFile;

public interface AudioFileService {
    /**
     * 上传音频文件并保存记录
     *
     * @param file   音频文件
     * @param userId 用户ID
     * @return 保存的音频文件记录
     */
    AudioFile uploadAndSave(MultipartFile file, Long userId);
}