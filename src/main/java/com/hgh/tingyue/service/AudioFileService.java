package com.hgh.tingyue.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    /**
     * 删除音频文件
     *
     * @param id     文件ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteFile(Long id, Long userId);

    /**
     * 获取用户的音频文件列表
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 分页后的音频文件列表
     */
    IPage<AudioFile> getUserFiles(Long userId, int page, int pageSize);

    /**
     * 获取音频文件详情
     *
     * @param id     文件ID
     * @param userId 用户ID
     * @return 音频文件信息
     */
    AudioFile getFileDetail(Long id, Long userId);
}